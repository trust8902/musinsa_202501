package com.musinsa.shop.service

import com.musinsa.shop.common.code.BrandResponseStatus
import com.musinsa.shop.common.code.ProductResponseStatus
import com.musinsa.shop.common.exception.BrandResponseException
import com.musinsa.shop.common.exception.ProductResponseException
import com.musinsa.shop.domain.Product
import com.musinsa.shop.dto.*
import com.musinsa.shop.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@Service
@Transactional(readOnly = true)
class ProductService(
    private val productRepository: ProductRepository,
    private val commonService: CommonService,
) {
    fun getAllProducts(): List<ProductResponseDto> {
        return productRepository.findAllProducts()
            .takeIf { it.isNotEmpty() }
            ?.map { ProductResponseDto.fromEntity(it) }
                ?: throw ProductResponseException(ProductResponseStatus.EMPTY_PRODUCT)
    }

    fun getAllProductsByBrandName(brandName: String): List<ProductResponseDto> {
        return productRepository.findAllByBrandName(brandName)
            .takeIf { it.isNotEmpty() }
            ?.map { ProductResponseDto.fromEntity(it) }
                ?: throw ProductResponseException(ProductResponseStatus.EMPTY_PRODUCT)
    }

    fun getLowestCategoryProduct(): LowestCategoryProductResponseDto {
        return productRepository.findAllLowestCategoryProduct()
            .orElseThrow { ProductResponseException(ProductResponseStatus.EMPTY_LOWEST_CATEGORY_PRODUCT) }
    }

    fun getLowestBrandProduct(): LowestBrandProductResponseDto {
        return productRepository.findByLowestBrand()
            .orElseThrow { ProductResponseException(ProductResponseStatus.NOT_FOUND_LOWEST_BRAND_PRODUCT) }
    }

    fun getCategoryExtreamsBrand(categoryName: String): CategoryExtreamsBrandResponseDto {
        return productRepository.findByCategoryExtreamsBrands(categoryName)
            .orElseThrow { BrandResponseException(BrandResponseStatus.NOT_FOUND_CATEGORY_EXTREAMS_BRAND) }
    }

    @Transactional
    fun addProduct(request: ProductAddRequestDto): ProductResponseDto {
        if (productRepository.existsByCategoryNameAndBrandName(request.categoryName, request.brandName)) {
            throw ProductResponseException(ProductResponseStatus.ALREADY_EXISTS_PRODUCT)
        }

        val category = commonService.getCategoryByCategoryName(request.categoryName)
        val brand = commonService.getBrandByBrandName(request.brandName)

        val product = Product(
            category = category,
            brand = brand,
            price = request.price,
            createdAt = ZonedDateTime.now(),
        )

        productRepository.save(product)

        return ProductResponseDto.fromEntity(product)
    }

    @Transactional
    fun updateProduct(productId: Long, request: ProductUpdateRequestDto): ProductResponseDto {
        val product = commonService.getProduct(productId)
        product.update(request.price)
        return ProductResponseDto.fromEntity(product)
    }

    @Transactional
    fun deleteProduct(productId: Long) {
        val product = commonService.getProduct(productId)
        productRepository.delete(product)
    }
}