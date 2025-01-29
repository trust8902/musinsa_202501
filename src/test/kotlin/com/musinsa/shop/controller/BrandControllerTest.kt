package com.musinsa.shop.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.musinsa.shop.dto.*
import com.musinsa.shop.service.BrandService
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders


@ExtendWith(SpringExtension::class)
@WebMvcTest(BrandController::class)
class BrandControllerTest: DescribeSpec({

    val brandService = mockk<BrandService>()
    val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(BrandController(brandService)).build()
    val objectMapper = jacksonObjectMapper()

    describe("브랜드 API") {
        context("브랜드 조회") {
            it("전체 브랜드 반환") {
                val mockBrands = listOf(
                    BrandResponseDto(1L, "A"),
                    BrandResponseDto(2L, "B"),
                    BrandResponseDto(3L, "C"),
                    BrandResponseDto(4L, "D"),
                    BrandResponseDto(5L, "E"),
                    BrandResponseDto(6L, "F"),
                    BrandResponseDto(7L, "G"),
                    BrandResponseDto(8L, "H"),
                    BrandResponseDto(9L, "I"),
                )
                every { brandService.getAllBrands() } returns mockBrands

                val result = mockMvc.get("/api/v1/brand")
                    .andExpect {
                        status { isOk() }
                        content { contentType(MediaType.APPLICATION_JSON) }
                    }
                    .andReturn()
                val brands: List<BrandResponseDto> = objectMapper.readValue(result.response.contentAsString)

                brands.size shouldBe mockBrands.size

                brands.forEachIndexed { index, brand ->
                    brand shouldBe mockBrands[index]
                }
            }
        }

        context("브랜드 등록") {
            it("새로운 브랜드가 정상등록되어야 함") {
                val requestDto = BrandRequestDto(
                    "NEW BRAND",
                )
                val responseDto = BrandResponseDto(
                    10L,
                    "NEW BRAND"
                )

                every { brandService.addBrand(requestDto) } returns responseDto

                val result = mockMvc.post("/api/v1/brand") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(requestDto)
                }.andExpect { status { isOk() } }
                    .andReturn()

                val brand: BrandResponseDto = objectMapper.readValue(result.response.contentAsString)
                brand shouldBe responseDto
                brand.brandName shouldBe requestDto.brandName
            }
        }

        context("브랜드 수정") {
            it("기존에 등록된 브랜드명이 정상 수정되어야 함") {
                val brandId = 1L
                val requestDto = BrandRequestDto(
                    "JEAN JEAN"
                )
                val responseDto = BrandResponseDto(
                    brandId,
                    "JEAN JEAN"
                )

                every { brandService.updateBrand(brandId, requestDto) } returns responseDto

                val result = mockMvc.patch("/api/v1/brand/${brandId.toInt()}") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(requestDto)
                }.andExpect { status { isOk() } }
                    .andReturn()

                val brand: BrandResponseDto = objectMapper.readValue(result.response.contentAsString)
                brand shouldBe responseDto
                brand.brandName shouldBe requestDto.brandName
            }
        }

        context("브랜드 삭제") {
            it("기존에 등록된 브랜드가 정상 삭제 되어야 함") {
                val brandId = 9L
                every { brandService.deleteBrand(brandId) } just runs

                mockMvc.delete("/api/v1/brand/${brandId.toInt()}")
                    .andExpect { status { isOk() } }
            }
        }
    }
})