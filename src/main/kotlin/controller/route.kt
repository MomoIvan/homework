package com.example.momodemo.controller

import com.example.momodemo.model.BasePageResult
import com.example.momodemo.model.BaseResult
import com.example.momodemo.model.Order
import com.example.momodemo.service.OrderService
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = ["*"]) // 因為是在本地開發，所以用簡易的方式來處理 Cross 的問題
class Route(private val orderService: OrderService) {

    @GetMapping("/search")
    fun search(@RequestParam(required = false) order_no : Long?,
               @RequestParam(required = false) cust_no : Long?,
               @RequestParam(required = false) goods_code : Long?,
               @RequestParam(required = false) current_page : Int?,
               @RequestParam(required = false) page_limit : Int?
    ) : BasePageResult<Order>? {
        return  orderService.getOrderByCustNoAndOrderNoAndGoodsCode(cust_no, order_no, goods_code, current_page, page_limit)
    }

    @PostMapping("/import")
    fun importOrders(@RequestParam("file") file: MultipartFile) : BaseResult<Order> {
        if (file.isEmpty || file.contentType != "text/csv") {
            return BaseResult<Order>( errorCode = 10000001, errorMessage = "Valid failed")
        }
        
        return orderService.replaceFromCSV(file)
    }
}