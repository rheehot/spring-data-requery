package org.springframework.data.requery.kotlin.domain.basic

import io.requery.*
import java.io.Serializable
import java.net.URL
import java.time.LocalDate
import java.util.*

/**
 * NOTE: kotlin 언어로 requery를 사용하려면 entity는 interface로 정의해야 합니다.
 */
@Entity
@Table(name = "basic_user")
interface BasicUser: Persistable, Serializable {

    @get:Key
    @get:Generated
    val id: Int

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