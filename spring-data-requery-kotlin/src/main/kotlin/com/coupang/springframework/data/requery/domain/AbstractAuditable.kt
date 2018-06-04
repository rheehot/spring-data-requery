package com.coupang.springframework.data.requery.domain

import com.coupang.kotlinx.data.requery.conveters.LocalDateTimeLongConverter
import com.coupang.kotlinx.objectx.uninitialized
import io.requery.*
import org.springframework.data.domain.Auditable
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

/**
 * AbstractAuditable
 *
 * @author debop@coupang.com
 * @since 18. 6. 4
 */
@Deprecated("requery에서는 제대로 안된다.")
@Superclass
abstract class AbstractAuditable<U, ID: Serializable>: AbstractPersistable<ID>(), Auditable<U, ID, LocalDateTime> {

    abstract override val id: ID?

    @get:ManyToOne
    open val createdBy: U? = null

    @JvmField
    @get:Column(name = "createdAt", nullable = true)
    @get:Convert(LocalDateTimeLongConverter::class)
    var createdDate: LocalDateTime = uninitialized()

    @get:ManyToOne
    open val lastModifiedBy: U? = null

    @JvmField
    @get:Column(name = "lastModifiedAt", nullable = true)
    @get:Convert(LocalDateTimeLongConverter::class)
    var lastModifiedDate: LocalDateTime = uninitialized()

    override fun getCreatedBy(): Optional<U> = Optional.ofNullable(createdBy)

    override fun getCreatedDate(): Optional<LocalDateTime> = Optional.ofNullable(createdDate)

    override fun getLastModifiedBy(): Optional<U> = Optional.ofNullable(lastModifiedBy)

    override fun getLastModifiedDate(): Optional<LocalDateTime> = Optional.ofNullable(lastModifiedDate)

    @PreInsert
    protected fun onPreInsert() {
        createdDate = LocalDateTime.now()
    }

    @PreUpdate
    protected fun onPreUpdate() {
        lastModifiedDate = LocalDateTime.now()
    }

}