package com.coupang.springframework.data.requery.domain.upsert

import com.coupang.kotlinx.core.hashOf
import com.coupang.kotlinx.objectx.uninitialized
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

    @get:Column
    abstract var name: String?

    // NOTE: Embedded 에 해당하는 놈은 abstract 를 빼야 한다.
    @get:Embedded
    var address: AbstractUpsertAddress = uninitialized()

    override fun hashCode(): Int {
        return hashOf(address)
    }

}