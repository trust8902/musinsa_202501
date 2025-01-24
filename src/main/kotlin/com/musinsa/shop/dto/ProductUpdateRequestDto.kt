package com.musinsa.shop.dto

import jakarta.validation.constraints.*

data class ProductUpdateRequestDto(
    @field:NotNull(message = "가격이 입력되지 않았습니다.")
    @field:PositiveOrZero(message = "가격은 0이상 입력해야 합니다.")
    @field:Max(value = 2147483647, message = "가격은 2147483647까지 입력가능 합니다.")
    val price: Int,
)