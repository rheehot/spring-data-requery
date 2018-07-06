package org.springframework.data.requery.domain.sample

import io.requery.Entity
import io.requery.ForeignKey
import io.requery.Key
import io.requery.ManyToOne
import org.springframework.data.requery.domain.AbstractPersistable
import java.util.*

@Entity
abstract class AbstractOrder: AbstractPersistable<Long>() {

    @get:Key
    abstract override val id: Long

    @get:ForeignKey
    @get:ManyToOne
    abstract var customer: AbstractCustomer?

    abstract var orderNo: String

    override fun hashCode(): Int = Objects.hashCode(orderNo)

}