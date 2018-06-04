package com.coupang.springframework.data.requery.domain.upsert

import com.coupang.kotlinx.objectx.ToStringBuilder
import com.coupang.springframework.data.requery.domain.AbstractPersistable
import com.fasterxml.jackson.annotation.JsonProperty
import io.requery.*
import java.util.*

@Entity(copyable = true)
abstract class AbstractUpsertEvent: AbstractPersistable<UUID>() {

    @get:Key
    // @get:JsonProperty("_id")
    abstract override var id: UUID?

    @get:JsonProperty("_name")
    abstract var name: String?

    @get:JsonProperty("_place")
    @get:ManyToOne
    abstract var place: AbstractUpsertPlace?

    @get:JsonProperty("_tags")
    @get:JunctionTable
    @get:ManyToMany(cascade = [CascadeAction.DELETE, CascadeAction.SAVE])
    abstract val tags: MutableSet<AbstractUpsertTag>

    fun addTag(tag: AbstractUpsertTag) {
        this.tags.add(tag)

        // NOTE: 이 것을 넣으면 무한루프에 빠진다. 
        // tag.events.add(this)
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