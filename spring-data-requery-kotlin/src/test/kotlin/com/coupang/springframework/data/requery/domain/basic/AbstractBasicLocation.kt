package com.coupang.springframework.data.requery.domain.basic

import com.coupang.kotlinx.core.hashOf
import com.coupang.kotlinx.objectx.ToStringBuilder
import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*


@Entity(cacheable = false, copyable = true)
abstract class AbstractBasicLocation: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    @get:Column(name = "locationId")
    abstract override val id: Long?

    abstract var line1: String?
    abstract var line2: String?
    abstract var line3: String?

    @get:Column(length = 5, nullable = false)
    abstract var zip: String

    @get:Column(length = 2, nullable = false)
    abstract var countryCode: String

    abstract var city: String?

    abstract var state: String?

    // NOTE: OneToOne 한 곳은 @ForeignKey 를 지정하고, 다른 한쪽은 mappedBy를 지정해줘야 합니다.
    // NOTE: OneToOne 의 mappedBy 를 지정한 쪽은 abstract 가 아닌 var 만으로 지정해줘야 합니다.  
    @get:OneToOne(mappedBy = "location")
    var user: AbstractBasicUser? = null

    @get:Transient
    var description: String? = null

    override fun hashCode(): Int {
        return hashOf(countryCode, zip)
    }

    @Transient
    override fun buildStringHelper(): ToStringBuilder {
        return super.buildStringHelper()
            .add("zip", zip)
            .add("countryCode", countryCode)
            .add("city", city)
            .add("state", state)
            .add("line1", line1)
    }
}