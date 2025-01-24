package com.musinsa.shop.dto

data class LowestBrandProductResponseDto(
    val brandName: String,
    val categories: List<LowestBrandProductCategoryResponseDto>,
    val totalPrice: Int
)

data class LowestBrandProductCategoryResponseDto(
    val categoryName: String,
    val price: Int
)