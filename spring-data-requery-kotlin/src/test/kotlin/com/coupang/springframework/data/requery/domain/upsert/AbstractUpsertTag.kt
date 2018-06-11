package com.coupang.springframework.data.requery.domain.upsert

import com.coupang.kotlinx.objectx.ToStringBuilder
import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*
import io.requery.query.MutableResult
import java.util.*

/**
 * AbstractUpsertTag
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
@Entity
abstract class AbstractUpsertTag: AbstractPersistable<UUID>() {

    @get:Key
    abstract var id: UUID?

    @get:Column
    abstract var name: String?

    @get:ManyToMany(cascade = [CascadeAction.DELETE, CascadeAction.SAVE])
    abstract val events: MutableResult<AbstractUpsertEvent>


    override fun hashCode(): Int {
        return name?.hashCode() ?: System.identityHashCode(this)
    }

    @Transient
    override fun buildStringHelper(): ToStringBuilder {
        return super.buildStringHelper()
            .add("name", name)
    }
}