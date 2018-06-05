package com.coupang.springframework.data.requery.domain.stateless

import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*
import io.requery.converter.LocalDateTimeConverter
import java.time.LocalDateTime

/**
 * AbstractStatelessEntity
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
@Entity(copyable = true, stateless = true)
abstract class AbstractStatelessEntity: AbstractPersistable<String>() {

    @get:Key
    @get:Column(name = "statelessId")
    abstract var id: String?

    abstract var flag1: Boolean
    abstract var flag2: Boolean

    @get:Convert(LocalDateTimeConverter::class)
    abstract var createdAt: LocalDateTime


    @PreInsert
    fun onPreInsert() {
        createdAt = LocalDateTime.now()
    }
}