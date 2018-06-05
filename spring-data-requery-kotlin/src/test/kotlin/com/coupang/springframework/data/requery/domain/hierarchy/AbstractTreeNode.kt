package com.coupang.springframework.data.requery.domain.hierarchy

import com.coupang.kotlinx.core.hashOf
import com.coupang.kotlinx.objectx.ToStringBuilder
import com.coupang.kotlinx.objectx.uninitialized
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
    abstract val id: Long?

    @get:Column(name = "nodeName", length = 48)
    abstract var name: String

    @get:ManyToOne
    @get:ForeignKey(delete = ReferentialAction.SET_NULL, update = ReferentialAction.CASCADE)
    abstract var parent: AbstractTreeNode?

    // TODO: association을 MutableResult 로 변경하면 제대로 안된다.

    @get:OneToMany(mappedBy = "parent", cascade = [CascadeAction.DELETE, CascadeAction.SAVE])
    abstract val children: MutableSet<AbstractTreeNode>

    @get:OneToMany(mappedBy = "node")
    abstract val attributes: MutableSet<AbstractNodeAttribute>

    @get:Embedded
    var nodePosition: NodePosition = uninitialized()

    fun addChild(child: AbstractTreeNode) {
        child.parent = this
        children.add(child)
        child.nodePosition.nodeLevel = this.nodePosition.nodeLevel + 1
        child.nodePosition.nodeOrder = children.size - 1
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