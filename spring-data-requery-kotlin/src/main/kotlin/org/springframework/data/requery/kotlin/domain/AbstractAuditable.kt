package org.springframework.data.requery.kotlin.domain

import io.requery.*
import org.springframework.data.requery.kotlin.converters.LocalDateTimeToLongConverter
import java.time.LocalDateTime

/**
 * org.springframework.data.requery.kotlin.domain.AbstractAuditable
 *
 * @author debop
 */
@Superclass
abstract class AbstractAuditable<ID>: AbstractPersistable<ID>() {

    @get:Lazy
    @get:Convert(LocalDateTimeToLongConverter::class)
    abstract var createdAt: LocalDateTime

    @get:Lazy
    @get:Convert(LocalDateTimeToLongConverter::class)
    abstract var modifiedAt: LocalDateTime

    @PreInsert
    protected fun onPreInsert() {
        createdAt = LocalDateTime.now()
    }

    @PreUpdate
    protected fun onPreUpdate() {
        modifiedAt = LocalDateTime.now()
    }
}