package org.springframework.data.requery.kotlin.domain.sample

import io.requery.*
import org.springframework.data.requery.kotlin.domain.PersistableObject

/**
 * org.springframework.data.requery.kotlin.domain.sample.Parent
 *
 * @author debop
 */
@Entity
interface Parent: PersistableObject {

    @get:Key
    @get: Generated
    val id: Long

    var name: String

    @get:JunctionTable
    @get:ManyToMany
    val children: MutableSet<Child>

    @JvmDefault
    fun addChild(child: Child): Parent {
        children.add(child)
        child.parents.add(this)
        return this
    }
}