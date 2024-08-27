package service

import com.example.momodemo.model.BaseResult
import com.example.momodemo.model.Order
import com.example.momodemo.repository.OrderRepository
import com.example.momodemo.service.OrderService
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
import org.springframework.web.multipart.MultipartFile
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderServiceTest {

    private var expectedOrders: List<Order> = listOf()

    @RelaxedMockK
    private lateinit var orderRepository: OrderRepository

    @RelaxedMockK
    private lateinit var mongoTemplate: MongoTemplate

    @InjectMockKs
    private lateinit var orderService: OrderService

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
    fun `getAllOrders 回傳所有的 orders`() {
        // Arrange
        every { orderRepository.findAll() } returns expectedOrders

        // Act
        val result = orderService.getAllOrders()

        // Assert
        assertEquals(
            BaseResult<Order>(
                data = expectedOrders
            ), result
        )
    }

    @Test
    fun `getOrderByCustNoAndOrderNoAndGoodsCode 回傳所有的 orders`() {
        // Arrange
        every { mongoTemplate.find(Query(), Order::class.java) } returns expectedOrders

        // Act
        val result = orderService.getOrderByCustNoAndOrderNoAndGoodsCode(null, null, null)

        // Assert
        assertEquals(
            BaseResult<Order>(
                data = expectedOrders,
            ), result
        )
    }

    @Test
    fun `getOrderByCustNoAndOrderNoAndGoodsCode 回傳 cust_no 搜尋的結果`() {
        // Arrange
        val orders = expectedOrders.take(1)
        val query = Query()
        query.addCriteria(Criteria.where("cust_no").`is`(200412001024L))
        every { mongoTemplate.find(query, Order::class.java) } returns orders

        // Act
        val result = orderService.getOrderByCustNoAndOrderNoAndGoodsCode(200412001024, null, null)

        // Assert
        assertEquals(
            BaseResult<Order>(
                data = orders
            ), result
        )
    }

    @Test
    fun `getOrderByCustNoAndOrderNoAndGoodsCode 回傳 order_no 搜尋的結果`() {
        // Arrange
        val orders = expectedOrders.take(1)
        val query = Query()
        query.addCriteria(Criteria.where("order_no").`is`(23111989061458L))
        every { mongoTemplate.find(query, Order::class.java) } returns orders

        // Act
        val result = orderService.getOrderByCustNoAndOrderNoAndGoodsCode(null, 23111989061458, null)

        // Assert
        assertEquals(
            BaseResult<Order>(
                data = orders
            ), result
        )
    }

    @Test
    fun `getOrderByCustNoAndOrderNoAndGoodsCode 回傳 goods_code 搜尋的結果`() {
        // Arrange
        val orders = expectedOrders.take(1)
        val query = Query()
        query.addCriteria(Criteria.where("goods_code").`is`(11448027L))
        every { mongoTemplate.find(query, Order::class.java) } returns orders

        // Act
        val result = orderService.getOrderByCustNoAndOrderNoAndGoodsCode(null, null, 11448027)

        // Assert
        assertEquals(
            BaseResult<Order>(
                data = orders
            ), result
        )
    }

    @Test
    fun `replaceFromCSV 回傳 BaseResult 陣列資料`() {
        // Arrange
        val orderID = ObjectId()

        val fileContent = """
            cust_no,order_date,order_no,goods_code,net_amt,feedback_percent,feedback_money
            200412001024,11/19,23111989061458,11448027,599,0,65
            200412001079,11/14,23111481798929,12045112,600,0,18
            """.trimIndent().toByteArray()

        val file = MockMultipartFile(
            "file",
            "filename.csv",
            "text/csv",
            fileContent
        )

        every { orderRepository.deleteAll() } just Runs
        every { orderRepository.saveAll(expectedOrders) } returns emptyList()

        // Act
        val result = orderService.replaceFromCSV(file)

        // Assert
        assertEquals(0, result.errorCode)
        assertEquals(expectedOrders.map { it.copy(id = orderID) }, result.data!!.map { it.copy(id = orderID) })
    }

    @Test
    fun `replaceFromCSV 模擬 deleteAll 失敗`() {
        // Arrange
        val fileContent = """
            cust_no,order_date,order_no,goods_code,net_amt,feedback_percent,feedback_money
            200412001024,11/19,23111989061458,11448027,599,0,65
            200412001079,11/14,23111481798929,12045112,600,0,18
            """.trimIndent().toByteArray()

        val file = MockMultipartFile(
            "file",
            "filename.csv",
            "text/csv",
            fileContent
        )

        every { orderRepository.deleteAll() } throws RuntimeException("Test Exception")

        // Act
        val result = orderService.replaceFromCSV(file)

        // Assert
        assertEquals(BaseResult<Order>(
            errorCode = 20000099,
            errorMessage = "Exception : Test Exception",
        ), result)
    }
}