package com.musinsa.shop.domain

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import java.time.ZonedDateTime

@Entity(name = "product")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Comment("가격")
    var price: Int,

    @Comment("수정일")
    var updatedAt: ZonedDateTime? = null,

    @Comment("등록일")
    @Column(nullable = false)
    val createdAt: ZonedDateTime,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    val brand: Brand,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    val category: Category
) {
    fun update(price: Int) {
        this.price = price
        this.updatedAt = ZonedDateTime.now()
    }
}
