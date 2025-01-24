package com.musinsa.shop.dto

import com.musinsa.shop.domain.Brand

data class BrandResponseDto(
    val brandId: Long?,
    val brandName: String
) {
    companion object {
        fun fromEntity(brand: Brand) : BrandResponseDto {
            return BrandResponseDto(
                brandId = brand.id,
                brandName = brand.brandName
            )
        }
    }
}