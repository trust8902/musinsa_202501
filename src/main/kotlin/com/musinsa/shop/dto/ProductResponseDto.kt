package com.musinsa.shop.dto

import com.musinsa.shop.domain.Product

data class ProductResponseDto(
    val brandId: Long?,
    val brandName: String,
    val categoryId: Long?,
    val categoryName: String,
    val productId: Long?,
    val price: Int
) {
    companion object {
        fun fromEntity(product: Product) : ProductResponseDto {
            return ProductResponseDto(
                brandId = product.brand.id,
                brandName = product.brand.brandName,
                categoryId = product.category.id,
                categoryName = product.category.categoryName,
                productId = product.id,
                price = product.price,
            )
        }
    }
}