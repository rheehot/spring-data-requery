package com.coupang.springframework.data.requery.domain.sample

import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*
import io.requery.converter.LocalDateTimeConverter
import java.time.LocalDateTime

/**
 * com.coupang.springframework.data.requery.domain.sample.AbstractAnnotatedAuditable
 * @author debop
 * @since 18. 5. 30
 */
@Superclass
abstract class AbstractAnnotatedAuditable: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract override val id: Long?

    @get:Convert(LocalDateTimeConverter::class)
    abstract var createdAt: LocalDateTime?

    @get:Convert(LocalDateTimeConverter::class)
    abstract var lastModifiedAt: LocalDateTime?


    @PreInsert
    protected fun onPreInsert() {
        createdAt = LocalDateTime.now()
    }

    @PreUpdate
    protected fun onPreUpdate() {
        lastModifiedAt = LocalDateTime.now()
    }
}