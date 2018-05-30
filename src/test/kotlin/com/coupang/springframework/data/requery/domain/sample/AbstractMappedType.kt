package com.coupang.springframework.data.requery.domain.sample

import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.Generated
import io.requery.Key
import io.requery.Superclass
import io.requery.Version

/**
 * com.coupang.springframework.data.requery.domain.sample.AbstractMappedType
 * @author debop
 * @since 18. 5. 30
 */
@Superclass
abstract class AbstractMappedType: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract override val id: Long?

    @Version
    protected var version: Int = 0

    abstract var attribute1: String?

}