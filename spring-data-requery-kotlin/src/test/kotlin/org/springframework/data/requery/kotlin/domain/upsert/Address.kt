package org.springframework.data.requery.kotlin.domain.upsert

import io.requery.Column
import io.requery.Embedded
import io.requery.Persistable

@Embedded
interface Address: Persistable {

    @get:Column
    var address: String?

    @get:Column
    var city: String?

    @get:Column
    var zip: String
}