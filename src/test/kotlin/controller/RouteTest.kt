package controller

import com.example.momodemo.model.BaseResult
import com.example.momodemo.model.Order
import com.example.momodemo.service.OrderService
import com.example.momodemo.controller.Route
import com.example.momodemo.repository.OrderRepository
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.mock.web.MockMultipartFile
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RouteTest {

    private var expectedOrders: List<Order> = listOf()

    @InjectMockKs
    private lateinit var route: Route

    @InjectMockKs
    private lateinit var orderService: OrderService

    @RelaxedMockK
    private lateinit var mongoTemplate: MongoTemplate

    @RelaxedMockK
    private lateinit var orderRepository: OrderRepository

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        expectedOrders = listOf(
            Order(
                id = ObjectId("66c3f5935d63f113728b8172"),
                cust_no = 200412001024,
                order_date = "11/19",
                order_no = 23111989061458,
                goods_code = 11448027,
                net_amt = 599.0f,
                feedback_percent = 0.0f,
                feedback_money = 65.0f
            ),
            Order(
                id = ObjectId("66c3f5935d63f113728b8173"),
                cust_no = 200412001079,
                order_date = "11/14",
                order_no = 23111481798929,
                goods_code = 12045112,
                net_amt = 600.0f,
                feedback_percent = 0.0f,
                feedback_money = 18.0f
            )
        )
    }

    @Test
    fun `search 回傳所有訂單`() {
        // Arrange
        every { mongoTemplate.find(Query(), Order::class.java) } returns expectedOrders

        // Act
        val result = route.search(null, null, null)

        // Assert
        assertEquals(
            BaseResult<Order>(
                data = expectedOrders,
            ), result
        )
    }

    @Test
    fun `search 搜尋 cust_no 但找不到訂單`() {
        // Arrange
        val query = Query.query(Criteria.where("cust_no").`is`(1))
        every { mongoTemplate.find(query, Order::class.java) } returns emptyList()

        // Act
        val result = route.search(1, null, null)

        // Assert
        assertEquals(
            BaseResult<Order>(
                data = emptyList(),
            ), result
        )
    }

    @Test
    fun `search 搜尋 order_no 但找不到訂單`() {
        // Arrange
        val query = Query.query(Criteria.where("order_no").`is`(1))
        every { mongoTemplate.find(query, Order::class.java) } returns emptyList()

        // Act
        val result = route.search(null, 1, null)

        // Assert
        assertEquals(
            BaseResult<Order>(
                data = emptyList(),
            ), result
        )
    }

    @Test
    fun `importOrders 因 CSV 檔案是空的所以失敗`() {
        // Arrange
        val file = MockMultipartFile(
            "file",
            "filename.csv",
            "text/csv",
            "".toByteArray()
        )

        // Act
        val result = route.importOrders(file)

        // Assert
        assertEquals(
            BaseResult<Order>(
                errorCode = 10000001,
                errorMessage = "Valid failed"
            ), result
        )
    }

    @Test
    fun `importOrders 因 ContentType 不同所以失敗`() {
        // Arrange
        val file = MockMultipartFile(
            "file",
            "filename.csv",
            "application/json",
            "context is not empty".toByteArray()
        )

        // Act
        val result = route.importOrders(file)

        // Assert
        assertEquals(
            BaseResult<Order>(
                errorCode = 10000001,
                errorMessage = "Valid failed"
            ), result
        )
    }

    @Test
    fun `importOrders 解析檔案失敗`() {
        // Arrange
        val file = MockMultipartFile(
            "file",
            "filename.csv",
            "text/csv",
            "context is not empty".toByteArray()
        )

        // Act
        val result = route.importOrders(file)

        // Assert
        assertEquals(
            BaseResult<Order>(
                errorCode = 20000001,
                errorMessage = "convert error"
            ), result
        )
    }

    @Test
    fun `importOrders 匯入有欄位及資料的檔案`() {
        // Arrange
        val orderID = ObjectId()

        val file = MockMultipartFile(
            "file",
            "filename.csv",
            "text/csv",
            """
            cust_no,order_date,order_no,goods_code,net_amt,feedback_percent,feedback_money
            200412001024,11/19,23111989061458,11448027,599,0,65
            200412001079,11/14,23111481798929,12045112,600,0,18
            """.trimIndent().toByteArray()
        )

        every { orderRepository.deleteAll() } just Runs
        every { orderRepository.saveAll(expectedOrders) } returns emptyList()

        // Act
        val result = route.importOrders(file)

        // Assert
        assertEquals(0, result.errorCode)
        assertEquals(expectedOrders.map { it.copy(id = orderID) }, result.data!!.map { it.copy(id = orderID) })
    }

    @Test
    fun `importOrders 匯入只有欄位的檔案`() {
        // Arrange
        val file = MockMultipartFile(
            "file",
            "filename.csv",
            "text/csv",
            """
            cust_no,order_date,order_no,goods_code,net_amt,feedback_percent,feedback_money
            """.trimIndent().toByteArray()
        )

        every { orderRepository.deleteAll() } just Runs
        every { orderRepository.saveAll(expectedOrders) } returns emptyList()

        // Act
        val result = route.importOrders(file)

        // Assert
        assertEquals(BaseResult<Order>(
            errorCode = 20000001,
            errorMessage = "convert error"
        ), result)
    }
}