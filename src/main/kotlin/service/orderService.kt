package com.example.momodemo.service

import com.example.momodemo.model.Order
import com.example.momodemo.repository.OrderRepository
import com.example.momodemo.utils.ByteArrayConvert
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val mongoTemplate: MongoTemplate
) {

    fun getAllOrders(): List<Order> {
        return orderRepository.findAll()
    }

    fun getOrdersByNo(orderNo: Long): List<Order>? {
        return orderRepository.findByOrderNo(orderNo)
    }

    fun getOrdersByCustNo(custNo: Long): List<Order>? {
        return orderRepository.findByCustNo(custNo)
    }

    fun getOrderByGoodsCode(goodsCode: Long): List<Order>? {
        return orderRepository.findByGoodsCode(goodsCode)
    }

    fun getOrderByCustNoAndOrderNoAndGoodsCode(custNo: Long?, orderNo: Long?, goodsCode: Long?): List<Order>? {
        val query = Query()

        custNo?.let {
            query.addCriteria(Criteria.where("cust_no").`is`(it))
        }

        orderNo?.let {
            query.addCriteria(Criteria.where("order_no").`is`(it))
        }

        goodsCode?.let {
            query.addCriteria(Criteria.where("goods_code").`is`(it))
        }

        return mongoTemplate.find(query, Order::class.java)
    }

    @Transactional
    fun replaceFromSVC(file: MultipartFile) : Boolean {
        val svcOrders = ByteArrayConvert().parseSVCFile(file.bytes) ?: return false

        if (svcOrders.isEmpty()) {
            return false
        }

        try {
            orderRepository.deleteAll()
            orderRepository.saveAll(svcOrders)
        } catch (e: Exception) {
            return false
        }

        return true
    }
}