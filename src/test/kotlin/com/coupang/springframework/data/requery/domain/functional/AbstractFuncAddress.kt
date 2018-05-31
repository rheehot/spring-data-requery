package com.coupang.springframework.data.requery.domain.functional

import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*

/**
 * AbstractFuncAddress
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
@Entity(copyable = true)
abstract class AbstractFuncAddress: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract override val id: Long?

    abstract var line1: String?
    abstract var line2: String?

    abstract var state: String?

    @get:Column(length = 5)
    abstract var zip: String

    @get:Column(length = 2)
    abstract var countryCode: String

    abstract var city: String?

    @get:OneToOne(cascade = [CascadeAction.SAVE, CascadeAction.DELETE])
    abstract var person: AbstractFuncPerson?

    @get:Convert(AddressTypeStringConverter::class)
    abstract var type: AddressType
}