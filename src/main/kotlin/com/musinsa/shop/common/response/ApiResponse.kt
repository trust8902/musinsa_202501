package com.musinsa.shop.common.response

data class ApiResponse<T>(
    val statusCode: Int,
    val data: T,
)
