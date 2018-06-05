package com.coupang.springframework.data.requery.domain.sample

import io.requery.Entity
import io.requery.Generated
import io.requery.Key

@Entity
abstract class AbstractConcreteType2(): AbstractMappedType() {

    constructor(attribute1: String?): this() {
        this.attribute1 = attribute1
    }

    @get:Key
    @get:Generated
    abstract val id: Long


}
