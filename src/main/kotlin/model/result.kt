package com.example.momodemo.model

data class BaseResult<T>(
    var data: List<T>? = null,
    var errorCode: Int = 0,
    var errorMesssage: String = "",
)
