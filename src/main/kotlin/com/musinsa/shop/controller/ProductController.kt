package com.musinsa.shop.controller

import com.musinsa.shop.dto.*
import com.musinsa.shop.service.ProductService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/product")
class ProductController(
    private val productService: ProductService
) {
    @GetMapping
    fun getAllProducts(): List<ProductResponseDto> {
        return productService.getAllProducts()
    }

    @GetMapping("/{brandName}")
    fun getAllProductsByBrandName(@PathVariable brandName: String): List<ProductResponseDto> {
        return productService.getAllProductsByBrandName(brandName)
    }

    @GetMapping("/lowestCategoryProduct")
    fun getLowestCategoryProduct() : LowestCategoryProductResponseDto {
        return productService.getLowestCategoryProduct()
    }

    @GetMapping("/lowestBrandProduct")
    fun getLowestBrandProduct() : LowestBrandProductResponseDto {
        return productService.getLowestBrandProduct()
    }

    @GetMapping("/categoryExtreamsBrand/{categoryName}")
    fun getCategoryExtreamsBrand(@PathVariable categoryName: String) : CategoryExtreamsBrandResponseDto {
        return productService.getCategoryExtreamsBrand(categoryName)
    }

    @PostMapping
    fun addProduct(
        @RequestBody @Valid request: ProductAddRequestDto
    ): ProductResponseDto {
        return productService.addProduct(request)
    }

    @PatchMapping("/{productId}")
    fun updateProduct(
        @PathVariable productId: Long,
        @RequestBody @Valid request: ProductUpdateRequestDto
    ): ProductResponseDto {
        return productService.updateProduct(productId, request)
    }

    @DeleteMapping("/{productId}")
    fun deleteProduct(@PathVariable productId: Long) {
        return productService.deleteProduct(productId)
    }

}