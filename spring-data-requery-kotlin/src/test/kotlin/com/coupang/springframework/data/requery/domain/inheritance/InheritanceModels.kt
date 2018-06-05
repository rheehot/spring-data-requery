package com.coupang.springframework.data.requery.domain.inheritance

import com.coupang.kotlinx.objectx.ToStringBuilder
import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*

@Superclass
abstract class AbstractInheritanceBase {

    @get:ManyToMany(cascade = [CascadeAction.SAVE, CascadeAction.DELETE])
    @get:JunctionTable
    abstract val relateds: MutableList<AbstractInheritanceRelated>
}

@Entity
abstract class AbstractInheritanceDerivedA: AbstractInheritanceBase() {

    @get:Key
    @get:Generated
    abstract val id: Long

    abstract var attr: String?

    override fun hashCode(): Int {
        return attr?.hashCode() ?: System.identityHashCode(this)
    }
}

@Entity
abstract class AbstractInheritanceDerivedB: AbstractInheritanceBase() {

    @get:Key
    @get:Generated
    abstract val id: Long

    abstract var flag: Boolean

    override fun hashCode(): Int {
        return flag.hashCode()
    }
}


@Entity
abstract class AbstractInheritanceRelated: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract val id: Long?

    @get:Column(name = "attr")
    abstract var attribute: String?

    override fun hashCode(): Int {
        return attribute?.hashCode() ?: System.identityHashCode(this)
    }

    @Transient
    override fun buildStringHelper(): ToStringBuilder {
        return super.buildStringHelper()
            .add("attribute", attribute)
    }
}