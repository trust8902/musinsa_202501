package com.musinsa.shop.dto

data class CategoryExtreamsBrandResponseDto(
    val categoryName: String,
    val lowestPrice: List<ExtreamsBrand>?,
    val highestPrice: List<ExtreamsBrand>?,
)

data class ExtreamsBrand(
    val brandName: String,
    val price: Int,
)

data class CategoryExtreamsBrandTempDto(
    val id: Long,
    val categoryId: Long,
    val categoryName: String,
    val brandId: Long,
    val brandName: String,
    val price: Int,
    val rowNumberAsc: Long,
    val rowNumberDesc: Long
)