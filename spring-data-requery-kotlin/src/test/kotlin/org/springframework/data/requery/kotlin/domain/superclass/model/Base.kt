package org.springframework.data.requery.kotlin.domain.superclass.model

import io.requery.*
import java.io.Serializable

/**
 * org.springframework.data.requery.kotlin.domain.superclass.model.Base
 *
 * @author debop
 */
@Superclass
interface Base: Persistable, Serializable {

    @get:Key
    val id: Long

    @get:JunctionTable
    @get:ManyToMany
    val relateds: MutableList<Related>
}

@Entity
interface DerivedA: Base {

    var attr: String?
}

@Entity
interface DerivedB: Base {

    var flag: Boolean?
}