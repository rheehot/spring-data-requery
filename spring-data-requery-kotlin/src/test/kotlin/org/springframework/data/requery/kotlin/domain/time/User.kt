package org.springframework.data.requery.kotlin.domain.time

import io.requery.Entity
import io.requery.Key
import io.requery.Persistable
import io.requery.Table
import java.io.Serializable
import java.net.URL
import java.util.*

/**
 * User
 *
 * @author debop@coupang.com
 * @since 18. 5. 14
 */
@Entity
@Table(name = "Time_Users")
interface User: Persistable, Serializable {

    @get:Key
    var id: UUID

    @get:Key
    var name: String

    var age: Int?
    var email: String?

    val phoneNumbers: MutableSet<String>

    val attributes: MutableMap<String, String>

    var homepage: URL?

}