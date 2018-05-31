package com.coupang.springframework.data.requery.domain.upsert

import com.coupang.kotlinx.objectx.ToStringBuilder
import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.Entity
import io.requery.Key
import io.requery.ManyToMany
import io.requery.Transient
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
    abstract override var id: UUID?

    abstract var name: String?

    @get:ManyToMany(mappedBy = "tags")
    abstract val events: MutableSet<AbstractUpsertEvent>


    override fun hashCode(): Int {
        return name?.hashCode() ?: System.identityHashCode(this)
    }

    @Transient
    override fun buildStringHelper(): ToStringBuilder {
        return super.buildStringHelper()
            .add("name", name)
    }
}