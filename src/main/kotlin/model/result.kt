package com.example.momodemo.model

data class BaseResult<T>(
    var errorCode: Int = 0,
    var errorMessage: String = "",
    var data: List<T>? = null,
)

data class BasePageResult<T>(
    var errorCode: Int = 0,
    var errorMessage: String = "",
    val data: List<T>? = null,
    var paginate: PaginateResponse? = null,
)

data class PaginateResponse (
    val totalPages: Int = 1,
    val totalElements: Long = 0,
    val currentPage: Int = 0,
    val pageLimit: Int = 0
)