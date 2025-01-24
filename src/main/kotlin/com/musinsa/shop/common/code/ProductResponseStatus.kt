package com.musinsa.shop.common.code

enum class ProductResponseStatus(val id: Int, val itemName: String) {
    ALREADY_EXISTS_PRODUCT(1200, "이미 등록된 상품입니다."),
    EMPTY_PRODUCT(1201, "상품이 등록되지 않았거나 찾을 수 없습니다."),
    NOT_FOUND_CATEGORY_LOWEST_PRODUCT(1202, "카테고리에 해당하는 최저가 상품을 찾을 수 없습니다."),
    NOT_FOUND_CATEGORY_HIGHEST_PRODUCT(1203, "카테고리에 해당하는 최고가 상품을 찾을 수 없습니다."),
}