package com.coupang.springframework.data.requery.domain.hierarchy

import com.coupang.kotlinx.core.hashOf
import com.coupang.kotlinx.objectx.ToStringBuilder
import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*

/**
 * AbstractTreeNode
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
@Entity
abstract class AbstractTreeNode: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    @get:Column(name = "nodeId")
    abstract override val id: Long?

    @get:Column(name = "nodeName", length = 48)
    abstract var name: String

    @get:ManyToOne
    @get:ForeignKey(delete = ReferentialAction.SET_NULL, update = ReferentialAction.CASCADE)
    abstract var parent: AbstractTreeNode?

    @get:OneToMany(mappedBy = "parent", cascade = [CascadeAction.DELETE, CascadeAction.SAVE])
    abstract val children: MutableSet<AbstractTreeNode>

    @get:OneToMany(mappedBy = "node", cascade = [CascadeAction.DELETE, CascadeAction.SAVE])
    abstract val attributes: MutableSet<AbstractNodeAttribute>

    fun addChild(child: AbstractTreeNode) {
        child.parent = this
        children.add(child)
    }

    fun removeChild(child: AbstractTreeNode) {
        children.remove(child)
        child.parent = null
    }

    fun addAttribute(attr: AbstractNodeAttribute) {
        this.attributes.add(attr)
        attr.node = this
    }

    fun removeAttribute(attr: AbstractNodeAttribute) {
        this.attributes.remove(attr)
        attr.node = null
    }

    override fun hashCode(): Int {
        return hashOf(name)
    }

    @Transient
    override fun buildStringHelper(): ToStringBuilder {
        return super.buildStringHelper()
            .add("name", name)
    }
}