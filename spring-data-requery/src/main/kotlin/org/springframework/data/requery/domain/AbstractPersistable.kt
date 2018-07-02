package org.springframework.data.requery.domain

import io.requery.Persistable
import io.requery.Superclass
import java.io.Serializable

/**
 * Abstract Entity class for Requery
 *
 * @author debop
 */
@Superclass
abstract class AbstractPersistable<ID>: AbstractValueObject(), Persistable, Serializable {

    abstract val id: ID

    val isNew: Boolean get() = (id == null)

    override fun equals(other: Any?): Boolean = when(other) {
        is AbstractPersistable<*> -> {
            if(isNew && other.isNew) hashCode() == other.hashCode()
            else id == other.id
        }
        else                      -> false
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: System.identityHashCode(this)
    }

    override fun buildStringHelper(): ToStringBuilder {
        return super.buildStringHelper()
            .add("id", id)
    }
}