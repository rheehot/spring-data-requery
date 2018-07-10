package org.springframework.data.requery.kotlin.domain.sample

import io.requery.*
import org.springframework.data.requery.kotlin.converters.ByteArrayToBlobConverter
import org.springframework.data.requery.kotlin.domain.PersistableObject
import java.sql.Timestamp
import java.util.*

/**
 * org.springframework.data.requery.kotlin.domain.sample.User
 *
 * @author debop
 */
@Entity
@Table(name = "SD_User")
interface User: PersistableObject {

    @get:Key
    @get:Generated
    val id: Int

    @get:Index(value = ["idx_sd_user_name_email"])
    var firstname: String
    @get:Index(value = ["idx_sd_user_name_email"])
    var lastname: String

    var age: Int?
    var active: Boolean

    var createdAt: Timestamp

    @get:Index(value = ["idx_sd_user_name_email"])
    @get:Column(nullable = false, unique = true)
    var emailAddress: String

    @get:JunctionTable(name = "User_Colleagues")
    @get:ManyToMany
    val colleagues: MutableSet<User>

    @get:JunctionTable
    @get:ManyToMany
    val roles: MutableSet<Role>

    @get:ManyToOne
    var manager: User?

    @get:Embedded
    val address: Address

    @get:Convert(ByteArrayToBlobConverter::class)
    var binaryData: ByteArray?

    var dateOfBirth: Date?
}