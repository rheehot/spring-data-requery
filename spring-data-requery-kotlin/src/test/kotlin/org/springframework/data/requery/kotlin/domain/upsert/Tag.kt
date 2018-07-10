package org.springframework.data.requery.kotlin.domain.upsert

import io.requery.*
import java.util.*

@Entity
@Table(name = "UpsertTag")
interface Tag: Persistable {

    @get:Key
    var id: UUID

    var name: String?

    @get:JunctionTable
    @get:ManyToMany(cascade = [CascadeAction.SAVE, CascadeAction.DELETE])
    val events: MutableSet<UpsertEvent>
}