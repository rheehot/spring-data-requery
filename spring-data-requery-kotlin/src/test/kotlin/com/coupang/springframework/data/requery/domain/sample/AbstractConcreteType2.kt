package com.coupang.springframework.data.requery.domain.sample

import io.requery.Entity

/**
 * com.coupang.springframework.data.requery.domain.sample.AbstractConcreteType2
 * @author debop
 * @since 18. 5. 30
 */
@Entity
abstract class AbstractConcreteType2(): AbstractMappedType() {

    constructor(attribute1: String?): this() {
        this.attribute1 = attribute1
    }
}
