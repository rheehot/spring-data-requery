package com.coupang.springframework.data.requery.domain.sample

import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*

/**
 * com.coupang.springframework.data.requery.domain.sample.AbstractMappedType
 * @author debop
 * @since 18. 5. 30
 */
@Superclass
abstract class AbstractMappedType() {

    constructor(attribute1: String?): this() {
        this.attribute1 = attribute1
    }

    @get:Column(name = "mapped_attr1")
    abstract var attribute1: String?

    // NOTE: Superclass 인 경우에는 version 에 final 이 기본이지만, @Entity의 경우에는 `final`을 지정해주어야 합니다.
    @Version
    @JvmField
    final var version: Int = 0

}
