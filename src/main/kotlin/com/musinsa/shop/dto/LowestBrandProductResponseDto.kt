package com.musinsa.shop.dto

data class LowestBrandProductResponseDto(
    val brandName: String,
    val categories: List<LowestBrandProductCategoryResponseDto>,
    val totalPrice: Int
) {
    companion object {
        fun fromTempDtos(products: List<LowestBrandProductTempDto>): LowestBrandProductResponseDto {
            return LowestBrandProductResponseDto(
                brandName = products[0].brandName,
                categories = LowestBrandProductCategoryResponseDto.fromTempDtos(products),
                totalPrice = products.sumOf { it.price }
            )
        }
    }
}

data class LowestBrandProductCategoryResponseDto(
    val categoryName: String,
    val price: Int
) {
    companion object {
        fun fromTempDtos(products: List<LowestBrandProductTempDto>): List<LowestBrandProductCategoryResponseDto> {
            return products.map {
                LowestBrandProductCategoryResponseDto(
                    categoryName = it.categoryName,
                    price = it.price
                )
            }
        }
    }
}

data class LowestBrandProductTempDto(
    val brandName: String,
    val categoryName: String,
    val price: Int
)