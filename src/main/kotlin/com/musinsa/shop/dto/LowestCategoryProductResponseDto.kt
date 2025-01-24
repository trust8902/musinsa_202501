package com.musinsa.shop.dto

import com.musinsa.shop.domain.Product

data class LowestCategoryProductResponseDto(
    val rows: List<LowestCategoryProductItemResponseDto>,
    val totalPrice: Int
) {
    companion object {
        fun fromTempDtos(products: List<LowestCategoryTempProductDto>): LowestCategoryProductResponseDto {
            return LowestCategoryProductResponseDto(
                rows = LowestCategoryProductItemResponseDto.fromTempDtos(products),
                totalPrice = products.sumOf { it.price }
            )
        }
    }
}

data class LowestCategoryProductItemResponseDto(
    val categoryId: Long?,
    val categoryName: String,
    val brandId: Long?,
    val brandName: String,
    val price: Int
) {
    companion object {
        fun fromTempDtos(products: List<LowestCategoryTempProductDto>): List<LowestCategoryProductItemResponseDto> {
            return products.map {
                LowestCategoryProductItemResponseDto(
                    categoryId = it.categoryId,
                    categoryName = it.categoryName,
                    brandId = it.brandId,
                    brandName = it.brandName,
                    price = it.price,
                )
            }
        }

        fun fromEntity(product: Product): LowestCategoryProductItemResponseDto {
            return LowestCategoryProductItemResponseDto(
                categoryId = product.category.id,
                categoryName = product.category.categoryName,
                brandId = product.brand.id,
                brandName = product.brand.brandName,
                price = product.price,
            )
        }
    }
}

data class LowestCategoryTempProductDto(
    val id: Long,
    val categoryId: Long,
    val categoryName: String,
    val brandId: Long,
    val brandName: String,
    val price: Int,
    val rowNumber: Long
)