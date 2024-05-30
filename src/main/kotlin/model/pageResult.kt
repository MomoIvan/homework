package com.example.momodemo.model

data class PageResult<T>(
    val content: List<T>,
    val totalPages: Int,
    val totalElements: Long,
    val currentPage: Int,
    val pageLimit: Int
)
