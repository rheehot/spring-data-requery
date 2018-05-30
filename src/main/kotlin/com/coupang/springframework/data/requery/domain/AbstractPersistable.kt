package com.coupang.springframework.data.requery.domain

import io.requery.Superclass
import java.util.*

/**
 * com.coupang.springframework.data.requery.domain.AbstractPersistable
 * @author debop
 * @since 18. 5. 23
 */
@Superclass
abstract class AbstractPersistable<ID>: io.requery.Persistable {

    abstract val id: ID?

    @io.requery.Transient
    fun isNew(): Boolean = id == null

    override fun equals(other: Any?): Boolean {
        if(null == other) return false
        if(this === other) return true

        return when(other) {
            is AbstractPersistable<*> -> Objects.equals(id, other.id)
            else                      -> false
        }
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Entity of type ${this.javaClass.name} with id=$id"
    }
}