package org.springframework.data.requery.kotlin.domain.hierarchy.tree

import io.requery.*
import java.io.Serializable

/**
 * org.springframework.data.requery.kotlin.domain.hierarchy.tree.TreeNode
 *
 * @author debop
 */
@Entity
interface TreeNode: Persistable, Serializable {

    @get:Key
    @get:Generated
    @get:Column(name = "nodeId")
    val id: Int

    var name: String

    @get:ManyToOne
    @get:ForeignKey(delete = ReferentialAction.SET_NULL, update = ReferentialAction.CASCADE)
    var parent: TreeNode?

    @get:OneToMany(mappedBy = "parent", cascade = [CascadeAction.DELETE, CascadeAction.SAVE])
    val children: MutableSet<TreeNode>

    @get:OneToMany
    val attributes: MutableSet<NodeAttribute>

    @get:Embedded
    val nodePosition: NodePosition

    //    @JvmDefault
    //    fun addChild(child: TreeNode) {
    //        child.parent = this
    //        children.add(child)
    //    }
    //
    //    @JvmDefault
    //    fun addAddribute(attr: NodeAttribute) {
    //        attr.node = this
    //        attributes.add(attr)
    //    }
}