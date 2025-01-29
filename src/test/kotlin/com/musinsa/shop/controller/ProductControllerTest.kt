package com.musinsa.shop.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.musinsa.shop.common.code.ProductResponseStatus
import com.musinsa.shop.common.exception.ProductResponseException
import com.musinsa.shop.dto.ProductAddRequestDto
import com.musinsa.shop.dto.ProductResponseDto
import com.musinsa.shop.dto.ProductUpdateRequestDto
import com.musinsa.shop.service.ProductService
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
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(SpringExtension::class)
@WebMvcTest(ProductController::class)
class ProductControllerTest: DescribeSpec({

    val productService: ProductService = mockk()
    val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(ProductController(productService)).build()
    val objectMapper = jacksonObjectMapper()

    describe("상품 API") {
        context("브랜드별 상품 전체목록 조회") {
            it("브랜드명에 해당하는 상품목록을 반환해야 함") {
                val mockProducts = listOf(
                    ProductResponseDto(
                        1L,
                        "A",
                        1L,
                        "상의",
                        1L,
                        11200
                    ),
                )
                every { productService.getAllProductsByBrandName("A") } returns mockProducts

                val result = mockMvc.get("/api/v1/product/A")
                    .andExpect { status { isOk() } }
                    .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
                    .andReturn()

                val objectMapper = jacksonObjectMapper()
                val products: List<ProductResponseDto> = objectMapper.readValue(result.response.contentAsString)

                products.size shouldBe mockProducts.size

                products.forEachIndexed { index, product ->
                    product shouldBe mockProducts[index]
                }
            }

            it("존재하지 않은 브랜드명을 조회할 경우 400 오류를 반환") {
                every { productService.getAllProductsByBrandName("ZZZ") } throws ProductResponseException(ProductResponseStatus.EMPTY_PRODUCT)

                mockMvc.get("/api/v1/product/ZZZ")
                    .andExpect { status { isBadRequest() } }
            }
        }

        context("상품 등록") {
            it("새로운 상품이 정상등록되어야 함") {
                val requestDto = ProductAddRequestDto(
                    "A",
                    "상의",
                    3000
                )
                val responseDto = ProductResponseDto(
                    1L,
                    "A",
                    1L,
                    "상의",
                    1L,
                    3000
                )

                every { productService.addProduct(any()) } returns responseDto

                val result = mockMvc.post("/api/v1/product") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(requestDto)
                }.andExpect { status { isOk() } }
                    .andReturn()

                val product: ProductResponseDto = objectMapper.readValue(result.response.contentAsString)
                product shouldBe responseDto
            }
        }

        context("상품 수정") {
            it("기존에 등록된 상품이 정상 수정되어야 함") {
                val requestDto = ProductUpdateRequestDto(
                    5000
                )
                val responseDto = ProductResponseDto(
                    1L,
                    "A",
                    1L,
                    "상의",
                    1L,
                    5000
                )

                every { productService.updateProduct(1L, any()) } returns responseDto

                val result = mockMvc.patch("/api/v1/product/1") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(requestDto)
                }.andExpect { status { isOk() } }
                    .andReturn()

                val product: ProductResponseDto = objectMapper.readValue(result.response.contentAsString)
                product shouldBe responseDto
            }
        }

        context("상품 삭제") {
            it("기존에 등록된 상품이 정상 삭제 되어야 함") {
                every { productService.deleteProduct(1L) } just runs

                mockMvc.delete("/api/v1/product/1")
                    .andExpect { status { isOk() } }
            }
        }
    }
})