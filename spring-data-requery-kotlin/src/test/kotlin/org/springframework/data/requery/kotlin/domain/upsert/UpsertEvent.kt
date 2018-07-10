package org.springframework.data.requery.kotlin.domain.upsert

import com.fasterxml.jackson.annotation.JsonProperty
import io.requery.*
import java.util.*

@Entity
@Table(name = "UpsertEvent")
interface UpsertEvent: Persistable {

    @get:Key
    //    @get:JsonProperty("_id")
    var id: UUID

    @get:JsonProperty("_name")
    var name: String?

    @get:JsonProperty("_place")
    @get:ManyToOne(cascade = [CascadeAction.SAVE, CascadeAction.DELETE])
    var place: Place?

    @get:JsonProperty("_tags")
    @get:ManyToMany(cascade = [CascadeAction.SAVE, CascadeAction.DELETE])
    val tags: MutableSet<Tag>
}