package com.coupang.springframework.data.requery.domain.sample

import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.Entity
import io.requery.Generated
import io.requery.Key

/**
 * com.coupang.springframework.data.requery.domain.sample.AbstractProduct
 * @author debop
 * @since 18. 5. 30
 */
@Entity
abstract class AbstractProduct: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract override val id: Long?

    abstract var name: String?
}