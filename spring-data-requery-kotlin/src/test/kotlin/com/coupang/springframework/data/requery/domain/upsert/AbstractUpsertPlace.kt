package com.coupang.springframework.data.requery.domain.upsert

import com.coupang.kotlinx.core.hashOf
import com.coupang.kotlinx.objectx.ToStringBuilder
import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*
import io.requery.query.MutableResult


@Entity
abstract class AbstractUpsertPlace: AbstractPersistable<String>() {

    @get:Key
    abstract var id: String?

    @get:Column
    abstract var name: String

    @get:OneToMany(mappedBy = "place", cascade = [CascadeAction.SAVE])
    abstract val events: MutableResult<AbstractUpsertEvent>

    override fun hashCode(): Int {
        return hashOf(name)
    }

    @Transient
    override fun buildStringHelper(): ToStringBuilder {
        return super.buildStringHelper()
            .add("name", name)
    }
}