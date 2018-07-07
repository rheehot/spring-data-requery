package org.springframework.data.requery.domain.sample

import io.requery.Column
import io.requery.Embedded
import io.requery.Transient
import org.springframework.data.requery.domain.AbstractComponent
import org.springframework.data.requery.domain.ToStringBuilder

@Embedded
abstract class AbstractAddress: AbstractComponent() {

    @get:Column(length = 128)
    abstract var country: String?

    @get:Column(length = 64)
    abstract var city: String?

    @get:Column(length = 1024)
    abstract var streetName: String?

    @get:Column(length = 1024)
    abstract var streetNo: String?

    @Transient
    override fun buildStringHelper(): ToStringBuilder {
        return super.buildStringHelper()
            .add("country", country)
            .add("city", city)
            .add("streetName", streetName)
            .add("streetNo", streetNo)
    }
}