package com.coupang.springframework.data.requery.domain.functional

import com.coupang.kotlinx.core.hashOf
import com.coupang.kotlinx.objectx.uninitialized
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
    abstract val id: Long?

    abstract var line1: String?
    abstract var line2: String?

    abstract var state: String?

    @get:Column(length = 5)
    abstract var zip: String

    @get:Column(length = 2)
    abstract var countryCode: String

    abstract var city: String?

    // NOTE: OneToOne 한 곳은 @ForeignKey 를 지정하고, 다른 한쪽은 mappedBy를 지정해줘야 합니다.
    // NOTE: OneToOne 의 mappedBy 를 지정한 쪽은 abstract 가 아닌 var 만으로 지정해줘야 합니다.
    @get:OneToOne(mappedBy = "address", cascade = [CascadeAction.DELETE, CascadeAction.SAVE])
    var person: AbstractFuncPerson? = null

    @get:Convert(AddressTypeStringConverter::class)
    abstract var type: AddressType

    // NOTE: Embedded 에 해당하는 놈은 abstract 를 빼야 한다.
    @get:Embedded
    var coordinate: AbstractFuncCoordinate = uninitialized()

    override fun hashCode(): Int {
        return hashOf(zip, countryCode)
    }
}