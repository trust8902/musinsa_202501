package com.musinsa.shop.repository

import com.musinsa.shop.domain.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long>, ProductRepositoryCustom {
    @Query("SELECT COUNT(a) > 0 FROM product a WHERE a.category.categoryName = :categoryName AND a.brand.brandName = :brandName")
    fun existsByCategoryNameAndBrandName(categoryName: String, brandName: String): Boolean

    @Query("SELECT a FROM product a JOIN FETCH a.category JOIN FETCH a.brand")
    fun findAllProducts(): List<Product>

    @Query("SELECT a FROM product a JOIN FETCH a.category JOIN FETCH a.brand WHERE a.brand.brandName = :brandName")
    fun findAllByBrandName(brandName: String): List<Product>
}