package com.musinsa.shop.service

import com.musinsa.shop.common.code.ProductResponseStatus
import com.musinsa.shop.common.exception.ProductResponseException
import com.musinsa.shop.domain.Brand
import com.musinsa.shop.domain.Category
import com.musinsa.shop.domain.Product
import com.musinsa.shop.dto.*
import com.musinsa.shop.repository.ProductRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.junit.jupiter.api.Tag
import java.time.ZonedDateTime
import java.util.Optional

@Tag("Service")
class ProductServiceTest: DescribeSpec({

    val productRepository = mockk<ProductRepository>(relaxed = true)
    val commonService = mockk<CommonService>(relaxed = true)
    val productService = ProductService(productRepository, commonService)

    describe("상품 서비스 테스트") {
        afterTest {
            clearMocks(productRepository)
            clearMocks(commonService)
        }

        context("조회") {
            it("getAllProducts") {
                val mockProducts = getMockProducts()

                every { productRepository.findAllProducts() } returns mockProducts

                val result = productService.getAllProducts()

                result.size shouldBe mockProducts.size
                result.forEachIndexed { index, dto ->
                    dto shouldBe ProductResponseDto.fromEntity(mockProducts[index])
                }
            }

            it("getAllProductsByBrandName") {
                val brandName = "A"
                val mockProducts = getMockProducts(brandName)

                every { productRepository.findAllByBrandName(brandName) } returns mockProducts

                val result = productService.getAllProductsByBrandName(brandName)

                result.size shouldBe mockProducts.size
                result.forEachIndexed { index, dto ->
                    dto shouldBe ProductResponseDto.fromEntity(mockProducts[index])
                }
            }
            it("getLowestCategoryProduct") {
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

                every { productRepository.findAllLowestCategoryProduct() } returns Optional.of(mockLowestCategoryProduct)

                val result = productService.getLowestCategoryProduct()

                result.totalPrice shouldBe mockLowestCategoryProduct.totalPrice
                result.rows.size shouldBe mockLowestCategoryProduct.rows.size
                result.rows.forEachIndexed { index, dto ->
                    dto shouldBe mockLowestCategoryProduct.rows[index]
                }
            }

            it("getLowestBrandProduct") {
                val brandName = "D"
                val mockLowestBrandProducts = LowestBrandProductResponseDto(
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

                every { productRepository.findByLowestBrand() } returns Optional.of(mockLowestBrandProducts)

                val result = productService.getLowestBrandProduct()

                result.totalPrice shouldBe mockLowestBrandProducts.totalPrice
                result.brandName shouldBe mockLowestBrandProducts.brandName
                result.categories.size shouldBe mockLowestBrandProducts.categories.size
                result.categories.forEachIndexed { index, dto ->
                    dto shouldBe mockLowestBrandProducts.categories[index]
                }
            }

            it("getCategoryExtreamsBrand") {
                val categoryName = "바지"
                val mockCategoryExtreamsBrand = CategoryExtreamsBrandResponseDto(
                    categoryName,
                    listOf(ExtreamsBrand("D", 3000)),
                    listOf(ExtreamsBrand("D", 3000))
                )

                every { productRepository.findByCategoryExtreamsBrands(categoryName) } returns Optional.of(mockCategoryExtreamsBrand)

                val result = productService.getCategoryExtreamsBrand(categoryName)

                result.categoryName shouldBe mockCategoryExtreamsBrand.categoryName
                result.lowestPrice?.size shouldBe mockCategoryExtreamsBrand.lowestPrice?.size
                result.highestPrice?.size shouldBe mockCategoryExtreamsBrand.highestPrice?.size

                result.lowestPrice?.forEachIndexed { index, dto ->
                    dto shouldBe mockCategoryExtreamsBrand.lowestPrice!![index]
                }

                result.highestPrice?.forEachIndexed { index, dto ->
                    dto shouldBe mockCategoryExtreamsBrand.highestPrice!![index]
                }
            }
        }

        context("상품 등록") {
            val mockDto = ProductAddRequestDto(
                "A",
                "상의",
                3000
            )
            val mockCategory = Category(1L, "상의", ZonedDateTime.now(), ZonedDateTime.now())
            val mockBrand = Brand(1L, "A", ZonedDateTime.now(), ZonedDateTime.now())
            val mockProduct = Product(
                1L,
                3000,
                ZonedDateTime.now(),
                ZonedDateTime.now(),
                mockBrand,
                mockCategory
            )

            every { commonService.getCategoryByCategoryName(mockDto.categoryName) } returns mockCategory
            every { commonService.getBrandByBrandName(mockDto.brandName) } returns mockBrand
            every { productRepository.save(any<Product>()) } returns mockProduct

            val result = productService.addProduct(mockDto)

            result.categoryName shouldBe mockDto.categoryName
            result.brandName shouldBe mockDto.brandName
            result.price shouldBe mockDto.price

            verify(exactly = 1) { productRepository.save(any<Product>()) }
        }

        context("상품 수정") {
            val mockDto = ProductUpdateRequestDto(
                5000
            )
            val productId = 1L
            val mockCategory = Category(1L, "상의", ZonedDateTime.now(), ZonedDateTime.now())
            val mockBrand = Brand(1L, "A", ZonedDateTime.now(), ZonedDateTime.now())
            val mockProduct = Product(
                productId,
                3000,
                ZonedDateTime.now(),
                ZonedDateTime.now(),
                mockBrand,
                mockCategory
            )

            every { commonService.getProduct(productId) } returns mockProduct

            val result = productService.updateProduct(productId, mockDto)
            result.price shouldBe mockProduct.price
        }

        context("상품 삭제") {
            it("기존에 등록된 상품이 정상 삭제") {
                val productId = 1L
                val product = Product(
                    productId,
                    11200,
                    ZonedDateTime.now(),
                    ZonedDateTime.now(),
                    Brand(1L, "A", ZonedDateTime.now(), ZonedDateTime.now()),
                    Category(1L, "상의", ZonedDateTime.now(), ZonedDateTime.now())
                )

                every { commonService.getProduct(productId) } returns product
                every { productRepository.delete(product) } returns Unit

                productService.deleteProduct(productId)

                verify(exactly = 1) { productRepository.delete(any()) }
            }

            it("미등록 상품 삭제 시도 시 오류코드 반환") {
                val productId = 999L

                every { commonService.getProduct(any()) } throws ProductResponseException(ProductResponseStatus.EMPTY_PRODUCT)

                shouldThrow<ProductResponseException> {
                    productService.deleteProduct(productId)
                }.status shouldBe ProductResponseStatus.EMPTY_PRODUCT

                verify(exactly = 0) { productRepository.delete(any()) }
            }
        }
    }
}) {
    companion object {
        fun getMockProducts(brandName: String? = null): List<Product> {
            val brands = listOf(
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
            val categories = listOf(
                Category(1L, "상의", ZonedDateTime.now(), ZonedDateTime.now()),
                Category(2L, "아우터", ZonedDateTime.now(), ZonedDateTime.now()),
                Category(3L, "바지", ZonedDateTime.now(), ZonedDateTime.now()),
                Category(4L, "스니커즈", ZonedDateTime.now(), ZonedDateTime.now()),
                Category(5L, "가방", ZonedDateTime.now(), ZonedDateTime.now()),
                Category(6L, "모자", ZonedDateTime.now(), ZonedDateTime.now()),
                Category(7L, "양말", ZonedDateTime.now(), ZonedDateTime.now()),
                Category(8L, "액세서리", ZonedDateTime.now(), ZonedDateTime.now()),
            )

            var n = 1L
            val products = listOf(
                Product(n++, 11200, ZonedDateTime.now(), ZonedDateTime.now(), brands.get(0), categories.get(0)),
                Product(n++, 5500, ZonedDateTime.now(), ZonedDateTime.now(), brands.get(0), categories.get(1)),
                Product(n++, 4200, ZonedDateTime.now(), ZonedDateTime.now(), brands.get(0), categories.get(2)),
                Product(n++, 9000, ZonedDateTime.now(), ZonedDateTime.now(), brands.get(0), categories.get(3)),
                Product(n++, 2000, ZonedDateTime.now(), ZonedDateTime.now(), brands.get(0), categories.get(4)),
                Product(n++, 1700, ZonedDateTime.now(), ZonedDateTime.now(), brands.get(0), categories.get(5)),
                Product(n++, 1800, ZonedDateTime.now(), ZonedDateTime.now(), brands.get(0), categories.get(6)),
                Product(n++, 2300, ZonedDateTime.now(), ZonedDateTime.now(), brands.get(0), categories.get(7)),

                Product(n++, 10500, ZonedDateTime.now(), ZonedDateTime.now(), brands.get(1), categories.get(0)),
                Product(n++, 5900, ZonedDateTime.now(), ZonedDateTime.now(), brands.get(1), categories.get(1)),
                Product(n++, 3800, ZonedDateTime.now(), ZonedDateTime.now(), brands.get(1), categories.get(2)),
                Product(n++, 9100, ZonedDateTime.now(), ZonedDateTime.now(), brands.get(1), categories.get(3)),
                Product(n++, 2100, ZonedDateTime.now(), ZonedDateTime.now(), brands.get(1), categories.get(4)),
                Product(n++, 2000, ZonedDateTime.now(), ZonedDateTime.now(), brands.get(1), categories.get(5)),
                Product(n++, 2000, ZonedDateTime.now(), ZonedDateTime.now(), brands.get(1), categories.get(6)),
                Product(n++, 2200, ZonedDateTime.now(), ZonedDateTime.now(), brands.get(1), categories.get(7)),

                Product(n++, 10000, ZonedDateTime.now(), ZonedDateTime.now(), brands.get(2), categories.get(0)),
                Product(n++, 6200, ZonedDateTime.now(), ZonedDateTime.now(), brands.get(2), categories.get(1)),
                Product(n++, 3300, ZonedDateTime.now(), ZonedDateTime.now(), brands.get(2), categories.get(2)),
                Product(n++, 9200, ZonedDateTime.now(), ZonedDateTime.now(), brands.get(2), categories.get(3)),
                Product(n++, 2200, ZonedDateTime.now(), ZonedDateTime.now(), brands.get(2), categories.get(4)),
                Product(n++, 1900, ZonedDateTime.now(), ZonedDateTime.now(), brands.get(2), categories.get(5)),
                Product(n++, 2200, ZonedDateTime.now(), ZonedDateTime.now(), brands.get(2), categories.get(6)),
                Product(n++, 2100, ZonedDateTime.now(), ZonedDateTime.now(), brands.get(2), categories.get(7)),
            )

            if (brandName != null) {
                return products.filter { it.brand.brandName == brandName }
            } else {
                return products
            }
        }
    }
}