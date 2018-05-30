package com.coupang.springframework.data.requery.domain.sample

import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*

/**
 * com.coupang.springframework.data.requery.domain.sample.AbstractOrder
 * @author debop
 * @since 18. 5. 30
 */
abstract class AbstractOrder: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract override val id: Long?

    @get:ManyToOne
    @get:Column(nullable = false)
    @get:ForeignKey(referencedColumn = "customer_id")
    abstract val customer: AbstractCustomer
}