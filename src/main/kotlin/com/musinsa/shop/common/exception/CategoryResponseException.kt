package com.musinsa.shop.common.exception

import com.musinsa.shop.common.code.CategoryResponseStatus
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class CategoryResponseException(override val message: String = "", val status: CategoryResponseStatus) : RuntimeException() {
    constructor(status: CategoryResponseStatus) : this(status.itemName, status)
}

fun throwException(status: CategoryResponseStatus): CategoryResponseException {
    throw CategoryResponseException(status)
}