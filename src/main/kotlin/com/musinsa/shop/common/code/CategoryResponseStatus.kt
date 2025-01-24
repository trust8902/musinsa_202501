package com.musinsa.shop.common.code

enum class CategoryResponseStatus(val id: Int, val itemName: String) {
    UNKNOWN_CATEGORY_NAME(1000, "카테고리명이 등록되지 않았거나 찾을 수 없습니다."),
}