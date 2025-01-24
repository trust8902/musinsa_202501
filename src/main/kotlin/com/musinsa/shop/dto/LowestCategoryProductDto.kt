package com.musinsa.shop.dto

data class LowestCategoryProductDto(
    val categoryId: Long,
    val categoryName: String,
    val brandId: Long,
    val brandName: String,
    val price: Int,
)
