package org.springframework.data.requery.kotlin.domain.stateless

import io.requery.Entity
import io.requery.Key
import io.requery.Persistable
import java.io.Serializable
import java.time.LocalDateTime

/**
 * org.springframework.data.requery.kotlin.domain.stateless.Entry
 *
 * @author debop
 */
@Entity(stateless = true, copyable = true)
interface Entry: Persistable, Serializable {

    @get:Key
    var id: String

    var flag1: Boolean
    var flag2: Boolean

    var createdAt: LocalDateTime
}