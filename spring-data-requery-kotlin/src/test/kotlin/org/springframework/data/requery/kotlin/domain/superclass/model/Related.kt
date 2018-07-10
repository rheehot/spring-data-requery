package org.springframework.data.requery.kotlin.domain.superclass.model

import io.requery.Entity
import io.requery.Key
import io.requery.Persistable
import java.io.Serializable

/**
 * org.springframework.data.requery.kotlin.domain.superclass.model.Related
 *
 * @author debop
 */
@Entity
interface Related: Persistable, Serializable {

    @get:Key
    val id: Long

    var attribute: String
}