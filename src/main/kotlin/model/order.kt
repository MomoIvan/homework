package com.example.momodemo.model

import com.example.momodemo.utils.ObjectIdSerializer
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "Orders")
data class Order(
    @Id
    @JsonSerialize(using = ObjectIdSerializer::class)
    val id: ObjectId = ObjectId.get(),
    var cust_no: Long? = null,
    var order_date: String? = null,
    var order_no: Long? = null,
    var goods_code: Long? = null,
    var net_amt: Float? = null,
    var feedback_percent: Float? = null,
    var feedback_money: Float? = null,
)