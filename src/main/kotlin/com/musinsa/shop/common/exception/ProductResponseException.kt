package com.musinsa.shop.common.exception

import com.musinsa.shop.common.code.ProductResponseStatus

class ProductResponseException(override val message: String = "", val status: ProductResponseStatus) : RuntimeException() {
    constructor(status: ProductResponseStatus) : this(status.itemName, status)
}

fun throwException(status: ProductResponseStatus): ProductResponseException {
    throw ProductResponseException(status)
}