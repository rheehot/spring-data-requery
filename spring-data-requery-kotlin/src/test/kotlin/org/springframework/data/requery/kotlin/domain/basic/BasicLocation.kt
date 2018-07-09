package org.springframework.data.requery.kotlin.domain.basic

import io.requery.*
import java.io.Serializable


@Entity
@Table(name = "basic_address")
interface BasicLocation: io.requery.Persistable, Serializable {

    @get:Key
    @get:Generated
    val id: Long

    var line1: String
    var line2: String
    var line3: String

    @get:Column(length = 3)
    var zip: String

    @get:Column(length = 2)
    var countryCode: String

    var city: String

    var state: String

    @get:OneToOne(mappedBy = "address")
    @get:Column(name = "basic_user")
    var user: BasicUser

    @get:Transient
    var description: String

}