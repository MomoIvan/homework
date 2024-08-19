package com.example.momodemo.utils

import com.example.momodemo.model.Order
import java.io.ByteArrayInputStream
import java.util.*

private val requiredFields = listOf(
    "cust_no", "order_date", "order_no", "goods_code", "net_amt", "feedback_percent", "feedback_money"
)

class ByteArrayConvert {

    // 預設第一行是欄位名稱，從第二行開始是資料
    // 假如沒有資料就會吐 null
    fun csvToOrderList(data: ByteArray): List<Order>? {
        val orders = mutableListOf<Order>()

        ByteArrayInputStream(data).use { inputStream ->
            Scanner(inputStream).use { scanner ->
                if (!scanner.hasNextLine()) {
                    return null
                }

                // 預設第一行是欄位名稱，假設欄位名稱不符合就跳出
                val fields = scanner.nextLine().split(",")

                if (!fields.containsAll(requiredFields)) {
                    return null
                }

                // 取得欄位名稱後記住每個欄位的順序
                val fieldIndices = fields.associateBy({ it }) {
                    fields.indexOf(it)
                }

                // 從第二行開始取得每一個欄位的資料並塞到 Order Model 內
                while (scanner.hasNextLine()) {
                    val parts = scanner.nextLine().split(",")

                    // 檢查是否都有資料，假設有一格為空就跳出
                    if (parts.any { it.trim().isEmpty() }) {
                        return null
                    }

                    val order = Order()

                    parts.forEachIndexed { index, value ->
                        when (index) {
                            fieldIndices["cust_no"] -> order.cust_no = value.toLongOrNull()
                            fieldIndices["order_date"] -> order.order_date = value
                            fieldIndices["order_no"] -> order.order_no = value.toLongOrNull()
                            fieldIndices["goods_code"] -> order.goods_code = value.toLongOrNull()
                            fieldIndices["net_amt"] -> order.net_amt = value.toFloatOrNull()
                            fieldIndices["feedback_percent"] -> order.feedback_percent = value.toFloatOrNull()
                            fieldIndices["feedback_money"] -> order.feedback_money = value.toFloatOrNull()
                        }
                    }

                    orders.add(order)
                }
            }
        }

        return orders.takeIf { it.isNotEmpty() }
    }
}