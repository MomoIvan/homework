package com.example.momodemo.controller

import com.example.momodemo.model.Order
import com.example.momodemo.repository.OrderRepository
import com.example.momodemo.service.OrderService
import jakarta.websocket.server.PathParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/orders")
class Route(private val orderService: OrderService) {

    @GetMapping
    fun getAll(): List<Order> {
        return orderService.getAllOrders()
    }

    @GetMapping("/search")
    fun search(@RequestParam(required = false) order_no : Long?,
               @RequestParam(required = false) cust_no : Long?,
               @RequestParam(required = false) goods_code : Long?) : List<Order>? {
        return  orderService.getOrderByCustNoAndOrderNoAndGoodsCode(cust_no, order_no, goods_code)
    }

    @GetMapping("/{no}")
    fun getOrderByNo(@PathVariable no: Long): List<Order>? {
        return orderService.getOrdersByNo(no)
    }

    @GetMapping("/cust/{no}")
    fun getOrdersByCustNo(@PathVariable no: Long): List<Order>? {
        return orderService.getOrdersByCustNo(no)
    }

    @GetMapping("/goods/{no}")
    fun getOrderByGoodsCode(@PathVariable no: Long): List<Order>? {
        return orderService.getOrderByGoodsCode(no)
    }

    @PostMapping("/import")
    fun importOrders(@RequestParam("file") file: MultipartFile) : List<Order>? {
        if (file.isEmpty || file.contentType != "text/csv") {
            println("is Empty or != octet-stream")
            return null
        }

        orderService.replaceFromSVC(file)
        return null
    }
}