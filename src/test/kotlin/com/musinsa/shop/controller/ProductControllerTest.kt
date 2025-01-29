package com.musinsa.shop.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.musinsa.shop.common.code.ProductResponseStatus
import com.musinsa.shop.common.exception.ProductResponseException
import com.musinsa.shop.dto.*
import com.musinsa.shop.service.ProductService
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@Tag("Controller")
@ExtendWith(SpringExtension::class)
@WebMvcTest(ProductController::class)
class ProductControllerTest: DescribeSpec({

    val productService = mockk<ProductService>()
    val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(ProductController(productService)).build()
    val objectMapper = jacksonObjectMapper()

    describe("상품 API") {
        context("상품 조회") {
            it("브랜드명에 해당하는 상품목록 반환") {
                val brandName = "A"
                val mockProducts = listOf(
                    ProductResponseDto(1L, "A", 1L, "상의", 1L, 11200),
                    ProductResponseDto(1L, "A", 2L, "아우터", 2L, 5500),
                    ProductResponseDto(1L, "A", 3L, "바지", 3L, 4200),
                    ProductResponseDto(1L, "A", 4L, "스니커즈", 4L, 9000),
                    ProductResponseDto(1L, "A", 5L, "가방", 5L, 2000),
                    ProductResponseDto(1L, "A", 6L, "모자", 6L, 1700),
                    ProductResponseDto(1L, "A", 7L, "양말", 7L, 1800),
                    ProductResponseDto(1L, "A", 8L, "액세서리", 8L, 2300),
                )
                every { productService.getAllProductsByBrandName(brandName) } returns mockProducts

                val result = mockMvc.get("/api/v1/product/${brandName}")
                    .andExpect {
                        status { isOk() }
                        content { contentType(MediaType.APPLICATION_JSON) }
                    }
                    .andReturn()
                val products: List<ProductResponseDto> = objectMapper.readValue(result.response.contentAsString)

                products.size shouldBe mockProducts.size

                products.forEachIndexed { index, product ->
                    product shouldBe mockProducts[index]
                }
            }

            it("존재하지 않은 브랜드명을 조회할 경우 400 오류를 반환") {
                val brandName = "ZZZ"
                every { productService.getAllProductsByBrandName(brandName) } throws ProductResponseException(ProductResponseStatus.EMPTY_PRODUCT)

                mockMvc.get("/api/v1/product/${brandName}")
                    .andExpect { status { isBadRequest() } }
            }

            it("카테고리 별 최저가격 브랜드와 상품 가격, 총액 반환") {
                val mockLowestCategoryProduct = LowestCategoryProductResponseDto(
                    listOf(
                        LowestCategoryProductItemResponseDto(1, "상의", 3, "C", 10000),
                        LowestCategoryProductItemResponseDto(2, "아우터", 5, "E", 5000),
                        LowestCategoryProductItemResponseDto(3, "바지", 4, "D", 3000),
                        LowestCategoryProductItemResponseDto(4, "스니커즈", 1, "A", 9000),
                        LowestCategoryProductItemResponseDto(5, "가방", 1, "A", 2000),
                        LowestCategoryProductItemResponseDto(6, "모자", 4, "D", 1500),
                        LowestCategoryProductItemResponseDto(7, "양말", 9, "I", 1700),
                        LowestCategoryProductItemResponseDto(8, "액세서리", 6, "F", 1900),
                    ),
                    34100
                )
                every { productService.getLowestCategoryProduct() } returns mockLowestCategoryProduct

                val result = mockMvc.get("/api/v1/product/lowestCategoryProduct")
                    .andExpect {
                        status { isOk() }
                        content { contentType(MediaType.APPLICATION_JSON) }
                    }
                    .andReturn()
                val lowestCategoryProduct: LowestCategoryProductResponseDto = objectMapper.readValue(result.response.contentAsString)

                lowestCategoryProduct.totalPrice shouldBe mockLowestCategoryProduct.totalPrice
                lowestCategoryProduct.rows.size shouldBe mockLowestCategoryProduct.rows.size

                lowestCategoryProduct.rows.forEachIndexed { index, row ->
                    row shouldBe mockLowestCategoryProduct.rows[index]
                }
            }

            it("단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는브랜드와  카테고리의 상품가격, 총액을 조회") {
                val brandName = "D"
                val mockBrandProduct = LowestBrandProductResponseDto(
                    brandName,
                    listOf(
                        LowestBrandProductCategoryResponseDto("상의", 10100),
                        LowestBrandProductCategoryResponseDto("아우터", 5100),
                        LowestBrandProductCategoryResponseDto("바지", 3000),
                        LowestBrandProductCategoryResponseDto("스니커즈", 9500),
                        LowestBrandProductCategoryResponseDto("가방", 2500),
                        LowestBrandProductCategoryResponseDto("모자", 1500),
                        LowestBrandProductCategoryResponseDto("양말", 2400),
                        LowestBrandProductCategoryResponseDto("액세서리", 2000),
                    ),
                    36100
                )
                every { productService.getLowestBrandProduct() } returns mockBrandProduct

                val result = mockMvc.get("/api/v1/product/lowestBrandProduct")
                    .andExpect {
                        status { isOk() }
                        content { contentType(MediaType.APPLICATION_JSON) }
                    }
                    .andReturn()
                val lowestBrandProduct: LowestBrandProductResponseDto = objectMapper.readValue(result.response.contentAsString)

                lowestBrandProduct.totalPrice shouldBe mockBrandProduct.totalPrice
                lowestBrandProduct.categories.size shouldBe mockBrandProduct.categories.size
                lowestBrandProduct.categories.forEachIndexed { index, category ->
                    category shouldBe mockBrandProduct.categories[index]
                }
            }

            it("카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회") {
                val categoryName = "바지"
                val mockCategoryExtreamsBrand = CategoryExtreamsBrandResponseDto(
                    categoryName,
                    listOf(ExtreamsBrand("D", 3000)),
                    listOf(ExtreamsBrand("D", 3000))
                )
                every { productService.getCategoryExtreamsBrand(any()) } returns mockCategoryExtreamsBrand

                val result = mockMvc.get("/api/v1/product/categoryExtreamsBrand/${categoryName}")
                    .andExpect {
                        status { isOk() }
                        content { contentType(MediaType.APPLICATION_JSON) }
                    }
                    .andReturn()
                val categoryExtreamsProduct: CategoryExtreamsBrandResponseDto = objectMapper.readValue(result.response.contentAsString)

                categoryExtreamsProduct.categoryName shouldBe mockCategoryExtreamsBrand.categoryName
                categoryExtreamsProduct.lowestPrice?.size shouldBe mockCategoryExtreamsBrand.lowestPrice?.size
                categoryExtreamsProduct.highestPrice?.size shouldBe mockCategoryExtreamsBrand.highestPrice?.size

                categoryExtreamsProduct.lowestPrice?.forEachIndexed { index, product ->
                    product shouldBe mockCategoryExtreamsBrand.lowestPrice!![index]
                }

                categoryExtreamsProduct.highestPrice?.forEachIndexed { index, product ->
                    product shouldBe mockCategoryExtreamsBrand.highestPrice!![index]
                }
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
                val brandId = 1L
                val requestDto = ProductUpdateRequestDto(
                    5000
                )
                val responseDto = ProductResponseDto(
                    brandId,
                    "A",
                    1L,
                    "상의",
                    1L,
                    5000
                )

                every { productService.updateProduct(brandId, any()) } returns responseDto

                val result = mockMvc.patch("/api/v1/product/${brandId.toInt()}") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(requestDto)
                }.andExpect { status { isOk() } }
                    .andReturn()

                val product: ProductResponseDto = objectMapper.readValue(result.response.contentAsString)
                product shouldBe responseDto
            }
        }

        context("상품 삭제") {
            it("기존에 등록된 상품이 정상 삭제") {
                val productId = 1L

                every { productService.deleteProduct(productId) } returns Unit

                mockMvc.delete("/api/v1/product/${productId.toInt()}")
                    .andExpect { status { isOk() } }

                verify(exactly = 1) { productService.deleteProduct(productId) }
            }

            it("미등록 상품 삭제 시도 시 오류코드 반환") {
                val productId = 999L

                every { productService.deleteProduct(productId) } throws ProductResponseException(ProductResponseStatus.EMPTY_PRODUCT)

                mockMvc.delete("/api/v1/product/${productId.toInt()}")
                    .andExpect { status { isBadRequest() } }
            }
        }
    }
}) {
    @Autowired
    private lateinit var productService: ProductService
}