package com.coupang.springframework.data.requery.domain.sample

import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.Entity
import io.requery.Generated
import io.requery.Key
import io.requery.ManyToMany

/**
 * com.coupang.springframework.data.requery.domain.sample.AbstractChild
 * @author debop
 * @since 18. 5. 30
 */
@Entity
abstract class AbstractChild: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract val id: Long?

    @get:ManyToMany(mappedBy = "children")
    abstract val parents: MutableSet<AbstractParent>

    fun addParent(parent: AbstractParent): AbstractChild {
        this.parents += parent
        if(!parent.children.contains(this)) {
            parent.addChild(this)
        }
        return this
    }
}