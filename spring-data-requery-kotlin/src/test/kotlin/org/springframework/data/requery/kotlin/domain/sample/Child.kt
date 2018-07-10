package org.springframework.data.requery.kotlin.domain.sample

import io.requery.Entity
import io.requery.Generated
import io.requery.Key
import io.requery.ManyToMany
import org.springframework.data.requery.kotlin.domain.PersistableObject

/**
 * org.springframework.data.requery.kotlin.domain.sample.Child
 *
 * @author debop
 */
@Entity
interface Child: PersistableObject {

    @get:Key
    @get:Generated
    val id: Long

    var name: String

    @get:ManyToMany(mappedBy = "children")
    val parents: MutableSet<Parent>

}