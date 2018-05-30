package com.coupang.springframework.data.requery.domain

import com.coupang.kotlinx.core.hashOf
import io.requery.Generated
import io.requery.Key
import io.requery.Superclass
import org.springframework.data.domain.Persistable
import java.io.Serializable

/**
 * com.coupang.springframework.data.requery.domain.AbstractPersistable
 * @author debop
 * @since 18. 5. 23
 */
@Superclass
abstract class AbstractPersistable<ID: Serializable>: Persistable<ID>, io.requery.Persistable {

    @get:Key
    @get:Generated
    abstract val id: ID?

    @io.requery.Transient
    override fun isNew(): Boolean = id == null

    override fun equals(other: Any?): Boolean {
        if(null == other) return false
        if(this === other) return true

        return when(other) {
            is AbstractPersistable<*> -> getId()?.equals(other.getId()) ?: false
            else                      -> false
        }
    }

    override fun hashCode(): Int {
        return hashOf(getId())
    }

    override fun toString(): String {
        return "Entity of type ${this.javaClass.name} with id=${getId()}"
    }
}