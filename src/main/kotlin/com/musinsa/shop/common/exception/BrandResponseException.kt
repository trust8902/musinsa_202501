package com.musinsa.shop.common.exception

import com.musinsa.shop.common.code.BrandResponseStatus

class BrandResponseException(override val message: String = "", val status: BrandResponseStatus) : RuntimeException() {
    constructor(status: BrandResponseStatus) : this(status.itemName, status)
}

fun throwException(status: BrandResponseStatus): BrandResponseException {
    throw BrandResponseException(status)
}