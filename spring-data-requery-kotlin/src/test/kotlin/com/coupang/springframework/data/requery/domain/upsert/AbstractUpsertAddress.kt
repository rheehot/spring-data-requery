package com.coupang.springframework.data.requery.domain.upsert

import com.coupang.kotlinx.objectx.ToStringBuilder
import com.coupang.springframework.data.requery.domain.AbstractComponent
import io.requery.Column
import io.requery.Embedded
import io.requery.Transient

/**
 * AbstractUpsertAddress
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
@Embedded
abstract class AbstractUpsertAddress: AbstractComponent() {

    @get:Column
    abstract var address: String?

    @get:Column
    abstract var city: String?

    @get:Column(nullable = false)
    abstract var zipcode: String

    override fun hashCode(): Int {
        return zipcode.hashCode()
    }

    @Transient
    override fun buildStringHelper(): ToStringBuilder {
        return super.buildStringHelper()
            .add("zipcode", zipcode)
            .add("city", city)
            .add("address", address)
    }
}