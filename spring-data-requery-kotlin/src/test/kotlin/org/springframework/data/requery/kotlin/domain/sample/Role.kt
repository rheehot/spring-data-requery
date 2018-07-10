package org.springframework.data.requery.kotlin.domain.sample

import io.requery.Entity
import io.requery.Generated
import io.requery.Key
import io.requery.Table
import org.springframework.data.requery.kotlin.domain.PersistableObject

/**
 * org.springframework.data.requery.kotlin.domain.sample.Role
 *
 * @author debop
 */
@Entity
@Table(name = "SD_Roles")
interface Role: PersistableObject {

    @get:Key
    @get:Generated
    val id: Int

    var name: String
}