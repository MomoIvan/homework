package com.example.momodemo.controller

import com.example.momodemo.model.Order
import com.example.momodemo.model.PageResult
import com.example.momodemo.service.OrderService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = ["*"])
class Route(private val orderService: OrderService) {

    @GetMapping
    fun getAll(
        @RequestParam(required = false) sort: String?,
        @RequestParam(required = false) range: String?,
        @RequestParam(required = false) filter: String?,
        response: HttpServletResponse
    ): List<Order> {
        val orders = orderService.getAllOrders()
        response.setHeader("Access-Control-Expose-Headers", "X-Total-Count")
        response.setHeader("X-Total-Count", orders.size.toString())
        return orders
    }

    @GetMapping("/search")
    fun search(@RequestParam(required = false) order_no : Long?,
               @RequestParam(required = false) cust_no : Long?,
               @RequestParam(required = false) goods_code : Long?,
               @RequestParam(required = false, defaultValue = "0") current_page : Int,
               @RequestParam(required = false, defaultValue = "50") page_limit : Int
    ) : PageResult<Order>? {
        return  orderService.getOrderByCustNoAndOrderNoAndGoodsCode(cust_no, order_no, goods_code, current_page, page_limit)
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
    fun importOrders(@RequestParam("file") file: MultipartFile) : Boolean {
        if (file.isEmpty || file.contentType != "text/csv") {
            return false
        }
        
        return orderService.replaceFromSVC(file)
    }
}