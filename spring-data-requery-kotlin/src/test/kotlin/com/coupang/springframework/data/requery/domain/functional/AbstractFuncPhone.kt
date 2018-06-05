package com.coupang.springframework.data.requery.domain.functional

import com.coupang.springframework.data.requery.convert.IntArrayStringConverter
import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*

/**
 * AbstractFuncPhone
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
@Entity
abstract class AbstractFuncPhone: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    @get:Column(name = "phoneId")
    abstract val id: Long?


    abstract var phoneNumber: String

    abstract var normalized: Boolean

    @get:Convert(IntArrayStringConverter::class)
    abstract var extensions: IntArray

    @get:ManyToOne
    abstract var owner: AbstractFuncPerson?
}