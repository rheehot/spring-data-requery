package org.springframework.data.requery.kotlin.domain.upsert

import io.requery.*

@Entity
@Table(name = "UpsertPlace")
interface Place: Persistable {

    @get:Key
    var id: String

    var name: String

    @get:OneToMany(cascade = [CascadeAction.SAVE, CascadeAction.DELETE])
    val events: MutableSet<UpsertEvent>

}