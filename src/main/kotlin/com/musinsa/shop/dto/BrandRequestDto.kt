package com.musinsa.shop.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class BrandRequestDto(
    @field:NotNull(message = "브랜드명이 입력되지 않았습니다.")
    @field:Size(min = 1, max = 100, message = "브랜드명은 1자 이상 100자 미만으로 입력해야 합니다.")
    val brandName: String
)