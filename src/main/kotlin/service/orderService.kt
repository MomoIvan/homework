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

    // 假設沒有帶入分頁資訊，那就回傳全部資料
    fun getOrderByCustNoAndOrderNoAndGoodsCode(
        custNo: Long?, orderNo: Long?, goodsCode: Long?, currentPage: Int?, pageLimit: Int?
    ): BasePageResult<Order>? {
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

        if (currentPage == null || pageLimit == null) {
            val content = mongoTemplate.find(query, Order::class.java)

            return BasePageResult<Order>(
                data = content,
                paginate = PaginateResponse(
                    totalElements = content.size.toLong()
                )
            )
        }

        val totalElements = mongoTemplate.count(query, Order::class.java)

        query.with(PageRequest.of(currentPage, pageLimit))

        val content = mongoTemplate.find(query, Order::class.java)

        val totalPages = if (totalElements % pageLimit == 0L) {
            (totalElements / pageLimit).toInt()
        } else {
            (totalElements / pageLimit + 1).toInt()
        }

        return BasePageResult<Order>(
            data = content,
            paginate = PaginateResponse(
                totalPages = totalPages,
                totalElements = totalElements,
                currentPage = currentPage,
                pageLimit = pageLimit
            )
        )
    }

    @Transactional
    fun replaceFromCSV(file: MultipartFile) : BaseResult<Order> {
        val csvOrders = ByteArrayConvert().parserCSVFile(file.bytes) ?: return BaseResult<Order>( errorCode = 20000001,  errorMessage = "convert error")

        if (csvOrders.isEmpty()) {
            return BaseResult<Order>(
                errorCode = 20000002,
                errorMessage = "file is empty"
            )
        }

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
            data = orderRepository.findAll()
        )
    }
}