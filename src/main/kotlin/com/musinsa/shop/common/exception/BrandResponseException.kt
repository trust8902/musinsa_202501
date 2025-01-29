package com.musinsa.shop.common.exception

import com.musinsa.shop.common.code.BrandResponseStatus
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class BrandResponseException(override val message: String = "", val status: BrandResponseStatus) : RuntimeException() {
    constructor(status: BrandResponseStatus) : this(status.itemName, status)
}

fun throwException(status: BrandResponseStatus): BrandResponseException {
    throw BrandResponseException(status)
}