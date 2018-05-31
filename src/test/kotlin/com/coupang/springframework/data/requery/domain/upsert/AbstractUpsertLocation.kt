package com.coupang.springframework.data.requery.domain.upsert

import com.coupang.kotlinx.core.hashOf
import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*

/**
 * AbstractUpsertLocation
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
@Entity
@Table(name = "UpsertLocation")
abstract class AbstractUpsertLocation: AbstractPersistable<Int>() {

    @get:Key
    @get:Generated
    @get:Column(name = "locationId")
    abstract override val id: Int?

    @get:Embedded
    abstract val address: AbstractUpsertAddress

    override fun hashCode(): Int {
        return hashOf(address)
    }

}