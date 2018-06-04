package com.coupang.springframework.data.requery.domain.upsert

import com.coupang.kotlinx.core.hashOf
import com.coupang.kotlinx.objectx.ToStringBuilder
import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*


@Entity
abstract class AbstractUpsertPlace: AbstractPersistable<String>() {

    @get:Key
    abstract override var id: String?

    @get:Column
    abstract var name: String

    @get:OneToMany(mappedBy = "place", cascade = [CascadeAction.SAVE])
    abstract val events: MutableSet<AbstractUpsertEvent>

    fun addEvent(event: AbstractUpsertEvent) {
        this.events.add(event)
        event.place = this
    }

    override fun hashCode(): Int {
        return hashOf(name)
    }

    @Transient
    override fun buildStringHelper(): ToStringBuilder {
        return super.buildStringHelper()
            .add("name", name)
    }
}