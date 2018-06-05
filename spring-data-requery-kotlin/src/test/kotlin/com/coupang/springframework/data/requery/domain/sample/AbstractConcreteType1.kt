package com.coupang.springframework.data.requery.domain.sample

import io.requery.Entity
import io.requery.Generated
import io.requery.Key

/**
 * com.coupang.springframework.data.requery.domain.sample.AbstractConcreteType1
 * @author debop
 * @since 18. 5. 30
 */
@Entity
abstract class AbstractConcreteType1(): AbstractMappedType() {

    constructor(attribute1: String?): this() {
        this.attribute1 = attribute1
    }

    @get:Key
    @get:Generated
    abstract val id: Long


}