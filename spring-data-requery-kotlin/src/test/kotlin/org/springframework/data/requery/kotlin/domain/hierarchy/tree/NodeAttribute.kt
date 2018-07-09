package org.springframework.data.requery.kotlin.domain.hierarchy.tree

import io.requery.*
import java.io.Serializable

/**
 * org.springframework.data.requery.kotlin.domain.hierarchy.tree.NodeAttribute
 *
 * @author debop
 */
@Entity
interface NodeAttribute: Persistable, Serializable {

    @get:Key
    @get:Generated
    val id: Int

    var name: String

    var value: String

    @get:ManyToOne(cascade = [CascadeAction.DELETE, CascadeAction.SAVE])
    @get:ForeignKey(referencedColumn = "nodeId")
    var node: TreeNode?
}