package org.springframework.data.requery.kotlin.domain.upsert

import io.requery.*


@Entity
@Table(name = "UpsertLocation")
interface Location: Persistable {

    @get:Key
    @get:Generated
    val id: Int

    @get:Embedded
    val address: Address
}