package com.example.momodemo.repository

import com.example.momodemo.model.Order
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface OrderRepository : MongoRepository<Order, Long> {

    @Query("{'order_no' : ?0}")
    fun findByOrderNo(order_no: Long): List<Order>?

    @Query("{'cust_no' : ?0}")
    fun findByCustNo(cust_no: Long): List<Order>?

    @Query("{'goods_code' : ?0}")
    fun findByGoodsCode(goods_code: Long): List<Order>?
}