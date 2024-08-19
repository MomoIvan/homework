package com.example.momodemo.repository

import com.example.momodemo.model.Order
import org.springframework.data.mongodb.repository.MongoRepository

interface OrderRepository : MongoRepository<Order, Long> {

}