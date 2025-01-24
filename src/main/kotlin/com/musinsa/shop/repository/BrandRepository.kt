package com.musinsa.shop.repository

import com.musinsa.shop.domain.Brand
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BrandRepository : JpaRepository<Brand, Long> {
    fun existsByBrandName(brandName: String): Boolean
    fun existsByIdNotAndBrandName(brandId: Long, brandName: String): Boolean

    fun findByBrandName(brandName: String): Optional<Brand>
}