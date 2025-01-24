package com.musinsa.shop.domain

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import java.time.ZonedDateTime

@Entity(name = "brand")
data class Brand(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Comment("브랜드명")
    @Column(nullable = false, length = 100)
    var brandName: String,

    @Comment("수정일")
    var updatedAt: ZonedDateTime? = null,

    @Comment("등록일")
    @Column(nullable = false)
    val createdAt: ZonedDateTime
) {
    fun update(brandName: String) {
        this.brandName = brandName
        this.updatedAt = ZonedDateTime.now()
    }
}