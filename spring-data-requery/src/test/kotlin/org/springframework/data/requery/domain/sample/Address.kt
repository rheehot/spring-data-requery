package org.springframework.data.requery.domain.sample

import io.requery.Embedded
import org.springframework.data.requery.domain.AbstractComponent

@Embedded
class Address: AbstractComponent() {

    var country: String? = null
    var city: String? = null
    var streetName: String? = null
    var streetNo: String? = null
}