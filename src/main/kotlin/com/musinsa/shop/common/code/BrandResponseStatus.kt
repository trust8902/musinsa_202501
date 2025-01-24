package com.musinsa.shop.common.code

enum class BrandResponseStatus(val id: Int, val itemName: String) {
    ALREADY_EXISTS_BRAND_NAME(1100, "이미 등록된 브랜드명입니다."),
    UNKNOWN_BRAND_NAME(1101, "브랜드명이 등록되지 않았거나 찾을 수 없습니다."),
    EMPTY_BRANDS(1102, "등록된 브랜드가 없습니다."),
    NOT_FOUND_CATEGORY_EXTREAMS_BRAND(1103, "카테고리에 해당하는 브랜드와 상품가격을 찾을 수 없습니다."),
}