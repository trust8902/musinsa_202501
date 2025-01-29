package com.musinsa.shop.service

import com.musinsa.shop.domain.Brand
import com.musinsa.shop.dto.BrandRequestDto
import com.musinsa.shop.dto.BrandResponseDto
import com.musinsa.shop.repository.BrandRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Tag
import java.time.ZonedDateTime
import java.util.*

@Tag("Service")
class BrandServiceTest: DescribeSpec({

    val brandRepository = mockk<BrandRepository>(relaxed = true)
    val commonService = mockk<CommonService>(relaxed = true)
    val brandService = BrandService(brandRepository, commonService)

    describe("상품 서비스 테스트") {
        afterTest {
            clearMocks(brandRepository)
            clearMocks(commonService)
        }

        context("조회") {
            it("getAllBrands") {
                val mockBrands = listOf(
                    Brand(1L, "A", ZonedDateTime.now(), ZonedDateTime.now()),
                    Brand(2L, "B", ZonedDateTime.now(), ZonedDateTime.now()),
                    Brand(3L, "C", ZonedDateTime.now(), ZonedDateTime.now()),
                    Brand(4L, "D", ZonedDateTime.now(), ZonedDateTime.now()),
                    Brand(5L, "E", ZonedDateTime.now(), ZonedDateTime.now()),
                    Brand(6L, "F", ZonedDateTime.now(), ZonedDateTime.now()),
                    Brand(7L, "G", ZonedDateTime.now(), ZonedDateTime.now()),
                    Brand(8L, "H", ZonedDateTime.now(), ZonedDateTime.now()),
                    Brand(9L, "I", ZonedDateTime.now(), ZonedDateTime.now()),
                )

                every { brandRepository.findAll() } returns mockBrands

                val result = brandService.getAllBrands()

                result.size shouldBe mockBrands.size
                result.forEachIndexed { index, brand ->
                    brand shouldBe BrandResponseDto.fromEntity(mockBrands.get(index))
                }
            }
        }

        context("삽입") {
            val mockDto = BrandRequestDto("A")
            val mockBrand = Brand(1L, "A", ZonedDateTime.now(), ZonedDateTime.now())

            every { brandRepository.existsByBrandName(any()) } returns false
            every { brandRepository.save(any<Brand>()) } returns mockBrand

            val result = brandService.addBrand(mockDto)
            result.brandName shouldBe mockDto.brandName

            verify(exactly = 1) { brandRepository.save(any<Brand>()) }
        }

        context("수정") {
            val brandId = 1L
            val mockDto = BrandRequestDto("A")
            val mockBrand = Brand(brandId, "A", ZonedDateTime.now(), ZonedDateTime.now())

            every { brandRepository.existsByIdNotAndBrandName(any(), any()) } returns false
            every { commonService.getBrandById(any()) } returns mockBrand

            val result = brandService.updateBrand(brandId, mockDto)

            result.brandName shouldBe mockDto.brandName
        }

        context("삭제") {
            val brandId = 1L
            val mockBrand = Brand(brandId, "A", ZonedDateTime.now(), ZonedDateTime.now())

            every { brandRepository.findById(any()) } returns Optional.of(mockBrand)
            every { commonService.getBrandById(any()) } returns mockBrand
            every { brandRepository.delete(any<Brand>()) } returns Unit

            brandService.deleteBrand(brandId)

            verify(exactly = 1) { brandRepository.delete(any<Brand>()) }
        }
    }
})