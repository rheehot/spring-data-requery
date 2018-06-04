package com.coupang.springframework.data.requery.domain.sample

import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.Entity
import io.requery.Generated
import io.requery.Key
import io.requery.ManyToOne

/**
 * com.coupang.springframework.data.requery.domain.sample.AbstractCategory
 * @author debop
 * @since 18. 5. 30
 */
@Entity
abstract class AbstractCategory(): AbstractPersistable<Long>() {

    protected constructor(product: AbstractProduct): this() {
        this.product = product
    }

    @get:Key
    @get:Generated
    abstract override val id: Long?

    @get:ManyToOne
    abstract var product: AbstractProduct?
}