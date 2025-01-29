package com.musinsa.shop.domain

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import java.time.ZonedDateTime

@Entity(name = "category")
@SequenceGenerator(
    name = "category_seq_generator",
    sequenceName = "category_seq",
    allocationSize = 1
)
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq_generator")
    val id: Long? = null,

    @Comment("카테고리명")
    @Column(nullable = false, length = 100)
    var categoryName: String,

    @Comment("수정일")
    var updatedAt: ZonedDateTime? = null,

    @Comment("등록일")
    @Column(nullable = false)
    val createdAt: ZonedDateTime,
) {
    fun update(categoryName: String) {
        this.categoryName = categoryName
        this.updatedAt = ZonedDateTime.now()
    }
}
