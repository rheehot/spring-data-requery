package org.springframework.data.requery.kotlin.domain.basic

import io.requery.*
import java.io.Serializable
import java.net.URL
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "basic_user")
interface BasicUser: Persistable, Serializable {

    @get:Key
    @get:Generated
    var id: Int

    var name: String
    var email: String
    var birthday: LocalDate
    var age: Int?

    @get:ForeignKey
    @get:OneToOne
    var address: BasicLocation

    @get:ManyToMany(mappedBy = "members")
    var groups: Set<BasicGroup>

    var about: String?

    @get:Column(unique = true)
    var uuid: UUID

    var homepage: URL?

    var picture: String?
}