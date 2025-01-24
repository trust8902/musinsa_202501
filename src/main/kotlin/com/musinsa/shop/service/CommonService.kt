package com.musinsa.shop.service

import com.musinsa.shop.common.code.BrandResponseStatus
import com.musinsa.shop.common.code.CategoryResponseStatus
import com.musinsa.shop.common.code.ProductResponseStatus
import com.musinsa.shop.common.exception.BrandResponseException
import com.musinsa.shop.common.exception.CategoryResponseException
import com.musinsa.shop.common.exception.ProductResponseException
import com.musinsa.shop.domain.Brand
import com.musinsa.shop.domain.Category
import com.musinsa.shop.domain.Product
import com.musinsa.shop.repository.BrandRepository
import com.musinsa.shop.repository.CategoryRepository
import com.musinsa.shop.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class CommonService(
    private val categoryRepository: CategoryRepository,
    private val brandRepository: BrandRepository,
    private val productRepository: ProductRepository
) {
    fun getCategory(categoryId: Long): Category {
        return categoryRepository.findById(categoryId)
            .orElseThrow { CategoryResponseException(CategoryResponseStatus.UNKNOWN_CATEGORY_NAME) }
    }

    fun getBrand(brandId: Long): Brand {
        return brandRepository.findById(brandId)
            .orElseThrow { BrandResponseException(BrandResponseStatus.UNKNOWN_BRAND_NAME) }
    }

    fun getProduct(productId: Long): Product {
        return productRepository.findById(productId)
            .orElseThrow { ProductResponseException(ProductResponseStatus.EMPTY_PRODUCT) }
    }
}