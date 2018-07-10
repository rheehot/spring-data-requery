package org.springframework.data.requery.kotlin.domain.time

import io.requery.Entity
import io.requery.Key
import org.springframework.data.requery.kotlin.domain.PersistableObject
import java.net.URL
import java.util.*

/**
 * TimedUser
 *
 * @author debop@coupang.com
 * @since 18. 5. 14
 */
@Entity
interface TimedUser: PersistableObject {

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