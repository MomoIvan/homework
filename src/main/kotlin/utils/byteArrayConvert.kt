package com.example.momodemo.utils

import com.example.momodemo.model.Order
import java.io.ByteArrayInputStream
import java.util.*

class ByteArrayConvert {

    // 預設第一行是欄位名稱，從第二行開始是資料
    // 假如沒有資料就會吐 null
    fun parseSVCFile(data: ByteArray): List<Order>? {
        val orders = mutableListOf<Order>()

        ByteArrayInputStream(data).use { inputStream ->
            Scanner(inputStream).use { scanner ->
                if (!scanner.hasNextLine()) {
                    return null
                }

                // 預設第一行都會有欄位名稱，取得欄位名稱後記住每個欄位的順序
                val field = scanner.nextLine().split(",")
                var fieldIndices = field.associateBy({ it }) {
                    field.indexOf(it)
                }

                // 從第二行開始取得每一個欄位的資料並塞到 Order Model 內
                while (scanner.hasNextLine()) {
                    val parts = scanner.nextLine().split(",")
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