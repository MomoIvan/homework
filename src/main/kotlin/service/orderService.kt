package com.example.momodemo.service

import com.example.momodemo.model.*
import com.example.momodemo.repository.OrderRepository
import com.example.momodemo.utils.ByteArrayConvert
import org.springframework.data.domain.PageRequest
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

    fun getAllOrders(): BaseResult<Order> {
        return BaseResult<Order>(
            data = orderRepository.findAll()
        )
    }


    fun getOrderByCustNoAndOrderNoAndGoodsCode(
        custNo: Long?, orderNo: Long?, goodsCode: Long?
    ): BaseResult<Order>? {
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

        return  BaseResult<Order>(
            data = mongoTemplate.find(query, Order::class.java)
        )
    }

    @Transactional
    fun replaceFromCSV(file: MultipartFile) : BaseResult<Order> {
        val csvOrders = ByteArrayConvert().csvToOrderList(file.bytes) ?: return BaseResult<Order>( errorCode = 20000001,  errorMessage = "convert error")

        try {
            orderRepository.deleteAll()
            orderRepository.saveAll(csvOrders)
        } catch (e: Exception) {
            return BaseResult<Order>(
                errorCode = 20000099,
                errorMessage = "Exception : ${e.message}"
            )
        }

        return BaseResult(
            data = csvOrders
        )
    }
}