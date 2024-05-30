package com.example.momodemo.utils

import com.example.momodemo.model.Order
import java.io.ByteArrayInputStream
import java.util.*

class ByteArrayConvert {

    fun parseSVCFile(data: ByteArray): List<Order>? {
        val orders = mutableListOf<Order>()

        ByteArrayInputStream(data).use { inputStream ->
            Scanner(inputStream).use { scanner ->
                val fieldIndices = getFieldIndices(scanner) ?: return null

                while (scanner.hasNextLine()) {
                    val line = scanner.nextLine()
                    val parts = line.split(",")
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
        return orders
    }

    private fun getFieldIndices(scanner: Scanner): Map<String, Int>? {
        if (!scanner.hasNextLine()) return null

        val firstLine = scanner.nextLine()
        val parts = firstLine.split(",")
        return parts.associateBy({ it }) { parts.indexOf(it) }
    }
}