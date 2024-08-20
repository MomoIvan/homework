package com.example.momodemo.model

data class BaseResult<T>(
    var errorCode: Int = 0,
    var errorMessage: String = "",
    var data: List<T>? = null,
)