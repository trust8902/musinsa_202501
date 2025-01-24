package com.musinsa.shop.service

import com.musinsa.shop.common.code.BrandResponseStatus
import com.musinsa.shop.common.exception.BrandResponseException
import com.musinsa.shop.domain.Brand
import com.musinsa.shop.dto.BrandRequestDto
import com.musinsa.shop.dto.BrandResponseDto
import com.musinsa.shop.repository.BrandRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@Service
@Transactional(readOnly = true)
class BrandService(
    private val brandRepository: BrandRepository,
    private val commonService: CommonService,
) {
    fun getAllBrands(): List<BrandResponseDto> {
        return brandRepository.findAll()
            .takeIf { it != null }
            ?.map { BrandResponseDto.fromEntity(it) } ?: throw BrandResponseException(BrandResponseStatus.EMPTY_BRANDS)
    }

    @Transactional
    fun addBrand(request: BrandRequestDto): BrandResponseDto {
        if (brandRepository.existsByBrandName(request.brandName)) {
            throw BrandResponseException(BrandResponseStatus.ALREADY_EXISTS_BRAND_NAME)
        }

        val brand = Brand(
            brandName = request.brandName,
            createdAt = ZonedDateTime.now()
        )

        brandRepository.save(brand);

        return BrandResponseDto.fromEntity(brand)
    }

    @Transactional
    fun updateBrand(brandId: Long, request: BrandRequestDto): BrandResponseDto {
        if (brandRepository.existsByIdNotAndBrandName(brandId, request.brandName)) {
            throw BrandResponseException(BrandResponseStatus.ALREADY_EXISTS_BRAND_NAME)
        }

        val brand = commonService.getBrand(brandId)
        brand.update(request.brandName)
        return BrandResponseDto.fromEntity(brand)
    }

    @Transactional
    fun deleteBrand(brandId: Long) {
        val brand = commonService.getBrand(brandId)
        brandRepository.delete(brand)
    }
}