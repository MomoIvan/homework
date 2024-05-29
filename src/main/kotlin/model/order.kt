package com.example.momodemo.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "Orders")
data class Order(
    @Id
    val id: ObjectId? = null,
    var cust_no: Long? = null,
    var order_date: String? = null,
    var order_no: Long? = null,
    var goods_code: Long? = null,
    var net_amt: Float? = null,
    var feedback_percent: Float? = null,
    var feedback_money: Float? = null,
)
