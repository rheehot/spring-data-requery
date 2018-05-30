package com.coupang.springframework.data.requery.domain.sample

import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*
import io.requery.converter.LocalDateTimeConverter
import org.springframework.data.domain.Auditable
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

/**
 * com.coupang.springframework.data.requery.domain.sample.AbstractAuditable
 * @author debop
 * @since 18. 5. 30
 */
@Superclass
abstract class AbstractAuditable<U, ID: Serializable>: AbstractPersistable<ID>(), Auditable<U, ID, LocalDateTime> {

    abstract override val id: ID?

    @get:ManyToOne
    var _createdBy: U? = null

    @get:Convert(LocalDateTimeConverter::class)
    var _createdDate: LocalDateTime? = null

    @get:ManyToOne
    var _lastModifiedBy: U? = null

    @get:Convert(LocalDateTimeConverter::class)
    var _lastModifiedDate: LocalDateTime? = null


    override fun getCreatedBy(): Optional<U> = Optional.ofNullable(_createdBy)

    override fun setCreatedBy(createdBy: U) {
        _createdBy = createdBy
    }


    override fun getCreatedDate(): Optional<LocalDateTime> = Optional.ofNullable(_createdDate)

    override fun setCreatedDate(creationDate: LocalDateTime?) {
        _createdDate = creationDate
    }

    override fun getLastModifiedBy(): Optional<U> = Optional.ofNullable(_lastModifiedBy)

    override fun setLastModifiedBy(lastModifiedBy: U) {
        _lastModifiedBy = lastModifiedBy
    }


    override fun getLastModifiedDate(): Optional<LocalDateTime> = Optional.ofNullable(_lastModifiedDate)

    override fun setLastModifiedDate(lastModifiedDate: LocalDateTime?) {
        _lastModifiedDate = lastModifiedDate
    }

    @PreInsert
    protected fun onPreInsert() {
        setCreatedDate(LocalDateTime.now())
    }

    @PreUpdate
    protected fun onPreUpdate() {
        setLastModifiedDate(LocalDateTime.now())
    }
}