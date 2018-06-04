package com.coupang.springframework.data.requery.domain.sample

import com.coupang.kotlinx.objectx.ToStringBuilder
import com.coupang.springframework.data.requery.domain.AbstractComponent
import io.requery.Column
import io.requery.Embedded
import io.requery.Transient

/**
 * com.coupang.springframework.data.requery.domain.sample.AbstractAddress
 * @author debop
 * @since 18. 5. 30
 */
@Embedded
abstract class AbstractAddress: AbstractComponent() {

    @get:Column(length = 6, nullable = false)
    abstract var zipcode: String

    @get:Column(length = 32)
    abstract var country: String?

    @get:Column(length = 32)
    abstract var city: String?

    @get:Column(length = 128)
    abstract var streetName: String?

    @get:Column(length = 128)
    abstract var streetNo: String?

    @Transient
    override fun buildStringHelper(): ToStringBuilder {
        return super.buildStringHelper()
            .add("zipcode", zipcode)
            .add("country", country)
            .add("city", city)
            .add("streetName", streetName)
            .add("streetNo", streetNo)
    }
}