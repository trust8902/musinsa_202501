package com.musinsa.shop.common.exception

import com.musinsa.shop.common.code.ProductResponseStatus
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class ProductResponseException(override val message: String = "", val status: ProductResponseStatus) : RuntimeException() {
    constructor(status: ProductResponseStatus) : this(status.itemName, status)
}

fun throwException(status: ProductResponseStatus): ProductResponseException {
    throw ProductResponseException(status)
}