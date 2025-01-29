package com.musinsa.shop.repository

import com.musinsa.shop.domain.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CategoryRepository : JpaRepository<Category, Long> {

    fun findAllByCategoryName(categoryName: String): Optional<Category>

}