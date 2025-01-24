package com.musinsa.shop.common.exception

import com.musinsa.shop.common.code.CategoryResponseStatus

class CategoryResponseException(override val message: String = "", val status: CategoryResponseStatus) : RuntimeException() {
    constructor(status: CategoryResponseStatus) : this(status.itemName, status)
}

fun throwException(status: CategoryResponseStatus): CategoryResponseException {
    throw CategoryResponseException(status)
}