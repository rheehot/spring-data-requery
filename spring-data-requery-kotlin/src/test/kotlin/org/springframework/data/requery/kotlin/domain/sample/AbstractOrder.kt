package org.springframework.data.requery.kotlin.domain.sample

import io.requery.*
import org.springframework.data.requery.kotlin.domain.AbstractPersistable
import java.util.*

@Entity
@Table(name = "ORDERS")
abstract class AbstractOrder: AbstractPersistable<Long>() {

    @get:Key
    abstract override val id: Long

    @get:ForeignKey
    @get:ManyToOne
    abstract var customer: AbstractCustomer?

    abstract var orderNo: String

    override fun hashCode(): Int = Objects.hashCode(orderNo)

}