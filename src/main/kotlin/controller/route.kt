package com.example.momodemo.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Route {

    @GetMapping("/test")
    fun test(): String {
        return "Hello World!"
    }
}