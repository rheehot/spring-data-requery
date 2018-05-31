package com.coupang.springframework.data.requery.domain.upsert

import com.coupang.kotlinx.objectx.ToStringBuilder
import com.coupang.springframework.data.requery.domain.AbstractPersistable
import com.fasterxml.jackson.annotation.JsonProperty
import io.requery.*
import java.util.*

/**
 * AbstractUpsertEvent
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
@Entity(copyable = true)
abstract class AbstractUpsertEvent: AbstractPersistable<UUID>() {

    @get:Key
    @get:JsonProperty("_id")
    abstract override var id: UUID?

    @get:JsonProperty("_name")
    abstract var name: String?

    @get:JsonProperty("_place")
    @get:ManyToOne(cascade = [CascadeAction.DELETE, CascadeAction.SAVE])
    abstract var place: AbstractUpsertPlace?

    @get:JsonProperty("_tags")
    @get:JunctionTable(name = "Upsert_Event_Tags")
    @get:ManyToMany(cascade = [CascadeAction.DELETE, CascadeAction.SAVE])
    abstract val tags: MutableSet<AbstractUpsertTag>

    fun addTag(tag: AbstractUpsertTag) {
        this.tags.add(tag)
    }

    override fun hashCode(): Int {
        return name?.hashCode() ?: System.identityHashCode(this)
    }

    @Transient
    override fun buildStringHelper(): ToStringBuilder {
        return super.buildStringHelper()
            .add("name", name)
    }
}