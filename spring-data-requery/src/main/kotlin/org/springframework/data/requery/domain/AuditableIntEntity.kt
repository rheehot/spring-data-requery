package org.springframework.data.requery.domain

import io.requery.*
import org.springframework.data.requery.converters.LocalDateTimeToLongConverter
import java.time.LocalDateTime

/**
 * Auditable 이면서 Identifier 수형이 Integer인 Entity
 *
 * @author debop
 */
@Superclass
abstract class AuditableIntEntity: AbstractPersistable<Int>() {

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