package com.coupang.springframework.data.requery.domain.hierarchy

import com.coupang.kotlinx.objectx.ToStringBuilder
import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*

/**
 * AbstractNodeAttribute
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
@Entity
abstract class AbstractNodeAttribute: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract override val id: Long?


    abstract var name: String?

    abstract var value: String?


    @get:ManyToOne(cascade = [CascadeAction.SAVE, CascadeAction.DELETE])
    @get:ForeignKey(referencedColumn = "nodeId")
    abstract var node: AbstractTreeNode?

    override fun hashCode(): Int {
        return name?.hashCode() ?: System.identityHashCode(this)
    }

    @Transient
    override fun buildStringHelper(): ToStringBuilder {
        return super.buildStringHelper()
            .add("name", name)
            .add("value", value)
    }
}