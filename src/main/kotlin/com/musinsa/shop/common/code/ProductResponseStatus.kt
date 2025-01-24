package com.musinsa.shop.common.code

enum class ProductResponseStatus(val id: Int, val itemName: String) {
    ALREADY_EXISTS_PRODUCT(1200, "이미 등록된 상품입니다."),
    EMPTY_PRODUCT(1201, "상품이 등록되지 않았거나 찾을 수 없습니다."),
    EMPTY_LOWEST_CATEGORY_PRODUCT(1203, "카테고리 별 최저가격 브랜드, 상품 가격을 찾을 수 없습니다."),
    NOT_FOUND_LOWEST_BRAND_PRODUCT(1204, "모든 상품을 최저가에 판매중인 브랜드를 찾을 수 없습니다."),
}