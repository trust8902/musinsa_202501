package com.musinsa.shop.controller

import com.musinsa.shop.dto.BrandRequestDto
import com.musinsa.shop.dto.BrandResponseDto
import com.musinsa.shop.service.BrandService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/brand")
class BrandController(
    private val brandService: BrandService
) {
    @GetMapping
    fun getAllBrands(): List<BrandResponseDto> {
        return brandService.getAllBrands()
    }

    @PostMapping
    fun addBrand(
        @RequestBody @Valid request: BrandRequestDto
    ): BrandResponseDto {
        return brandService.addBrand(request)
    }

    @PatchMapping("/{brandId}")
    fun updateBrand(
        @PathVariable brandId : Long,
        @RequestBody @Valid request: BrandRequestDto
    ): BrandResponseDto {
        return brandService.updateBrand(brandId, request)
    }

    @DeleteMapping("/{brandId}")
    fun deleteBrand(
        @PathVariable brandId : Long
    ) {
        brandService.deleteBrand(brandId)
    }

}