package com.example.momodemo.controller

import com.example.momodemo.model.Order
import com.example.momodemo.repository.OrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/orders")
class Route(@Autowired val orderRepository: OrderRepository) {

    @GetMapping
    fun getAll(): List<Order> {
        return orderRepository.findAll()
    }

    @GetMapping("/{no}")
    fun getOrderByOrderNo(@PathVariable no: Long): List<Order>? {
        return orderRepository.findByOrderNo(no)
    }

    @GetMapping("/cust/{no}")
    fun getOrdersByCustNo(@PathVariable no: Long): List<Order>? {
        return orderRepository.findByCustNo(no)
    }

    @GetMapping("/goods/{no}")
    fun getOrderByGoodsCode(@PathVariable no: Long): List<Order>? {
        return orderRepository.findByGoodsCode(no)
    }
}