package com.musinsa.shop.repository

import com.musinsa.shop.domain.QBrand
import com.musinsa.shop.domain.QCategory
import com.musinsa.shop.domain.QProduct
import com.musinsa.shop.dto.*
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.*

interface ProductRepositoryCustom {
    fun findAllLowestCategoryProduct(): Optional<LowestCategoryProductResponseDto>
    fun findByCategoryExtreamsBrands(categoryName: String): Optional<CategoryExtreamsBrandResponseDto>
}

@Repository
class ProductRepositoryCustomImpl(
    @PersistenceContext private val entityManager: EntityManager,
) : ProductRepositoryCustom {

    private val queryFactory: JPAQueryFactory = JPAQueryFactory(entityManager)

    // 구현1. 카테고리 별 최저가격 브랜드와 상품가격, 총액을 조회
    override fun findAllLowestCategoryProduct(): Optional<LowestCategoryProductResponseDto> {
        val qProduct = QProduct.product
        val qCategory = QCategory.category
        val qBrand = QBrand.brand

        val expression = Expressions.numberTemplate(
            Long::class.java,
            "ROW_NUMBER() OVER (PARTITION BY {0} ORDER BY {1} ASC)",
            qProduct.category.id,
            qProduct.price
        )

        val products = queryFactory
            .select(
                Projections.constructor(
                    LowestCategoryTempProductDto::class.java,
                    qProduct.id,
                    qProduct.category.id,
                    qProduct.category.categoryName,
                    qProduct.brand.id,
                    qProduct.brand.brandName,
                    qProduct.price,
                    expression
                )
            )
            .from(qProduct)
            .innerJoin(qProduct.category, qCategory)
            .innerJoin(qProduct.brand, qBrand)
            .orderBy(qProduct.category.id.asc())
            .fetch()
            .filter { it.rowNumber == 1L }

        return if (products.isNotEmpty()) {
            Optional.of(LowestCategoryProductResponseDto.fromEntities(
                products
            ))
        } else {
            return Optional.empty()
        }
    }

    // 구현 3. 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회
    override fun findByCategoryExtreamsBrands(categoryName: String): Optional<CategoryExtreamsBrandResponseDto> {
        val qProduct = QProduct.product
        val qCategory = QCategory.category
        val qBrand = QBrand.brand

        val extreamsBrandProducts = queryFactory
            .select(
                Projections.constructor(
                    CategoryExtreamsBrandTempDto::class.java,
                    qProduct.id,
                    qProduct.category.id,
                    qProduct.category.categoryName,
                    qProduct.brand.id,
                    qProduct.brand.brandName,
                    qProduct.price,
                    Expressions.numberTemplate(
                        Long::class.java,
                        "ROW_NUMBER() OVER (PARTITION BY {0} ORDER BY {1} ASC)",
                        qCategory.categoryName,
                        qProduct.price
                    ).`as`("rnAsc"),
                    Expressions.numberTemplate(
                        Long::class.java,
                        "ROW_NUMBER() OVER (PARTITION BY {0} ORDER BY {1} DESC)",
                        qCategory.categoryName,
                        qProduct.price
                    ).`as`("rnDesc")
                )
            )
            .from(qProduct)
            .innerJoin(qProduct.category, qCategory)
            .innerJoin(qProduct.brand, qBrand)
            .where(qCategory.categoryName.eq(categoryName))
            .fetch()
            .filter { it.rowNumberAsc == 1L || it.rowNumberDesc == 1L }

        // 최저가 브랜드
        val lowestPrice = extreamsBrandProducts
            .filter { it.rowNumberAsc == 1L }
            .map {
                ExtreamsBrand(
                    brandName = it.brandName,
                    price = it.price
                )
            }

        // 최고가 브랜드
        val highestPrice = extreamsBrandProducts
            .filter { it.rowNumberDesc == 1L }
            .map {
                ExtreamsBrand(
                    brandName = it.brandName,
                    price = it.price
                )
            }

        return if (lowestPrice.isNotEmpty() || highestPrice.isNotEmpty()) {
            Optional.of(
                CategoryExtreamsBrandResponseDto(
                    categoryName = categoryName,
                    lowestPrice = if (lowestPrice.isNotEmpty()) lowestPrice else null,
                    highestPrice = if (highestPrice.isNotEmpty()) highestPrice else null
                )
            )
        } else {
            Optional.empty()
        }
    }

}