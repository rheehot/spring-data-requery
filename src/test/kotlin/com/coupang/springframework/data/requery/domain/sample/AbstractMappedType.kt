package com.coupang.springframework.data.requery.domain.sample

import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*

/**
 * com.coupang.springframework.data.requery.domain.sample.AbstractMappedType
 * @author debop
 * @since 18. 5. 30
 */
@Superclass
abstract class AbstractMappedType(): AbstractPersistable<Long>() {

    constructor(attribute1: String?): this() {
        this.attribute1 = attribute1
    }

    @get:Key
    @get:Generated
    abstract override val id: Long?

    // BUG : Java class 에서만 사용가능하다. 
    //    @Version
    //    abstract var version:Int

    abstract var attribute1: String?

}
