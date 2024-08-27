package utils

import com.example.momodemo.utils.ByteArrayConvert
import io.mockk.MockKAnnotations
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ByteArrayConvertTest {

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun `csvToOrderList 回傳陣列資料`() {
        // Arrange
        val fileContent = """
            cust_no,order_date,order_no,goods_code,net_amt,feedback_percent,feedback_money
            200412001024,11/19,23111989061458,11448027,599,0,65
            200412001079,11/14,23111481798929,12045112,600,0,18
            """.trimIndent().toByteArray()

        // Act
        val orders = ByteArrayConvert().csvToOrderList(fileContent)

        // Assert
        assertNotNull(orders)
    }

    @Test
    fun `csvToOrderList 輸入沒有欄位的空資料回傳 null`() {
        // Arrange
        val fileContent = "".toByteArray()

        // Act
        val orders = ByteArrayConvert().csvToOrderList(fileContent)

        // Assert
        assertNull(orders)
    }
}