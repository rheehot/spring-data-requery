package com.coupang.springframework.data.requery.domain.sample

import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*

/**
 * com.coupang.springframework.data.requery.domain.sample.AbstractParent
 * @author debop
 * @since 18. 5. 30
 */
@Entity
abstract class AbstractParent: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract override val id: Long?

    abstract var name: String?

    @get:ManyToMany
    @get:JunctionTable
    abstract val children: MutableSet<AbstractChild>

    fun addChild(child: AbstractChild): AbstractParent {
        this.children.add(child)

        if(!child.parents.contains(this)) {
            child.addParent(this)
        }
        return this
    }
}