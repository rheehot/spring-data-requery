package org.springframework.data.requery.kotlin.domain.hierarchy

import mu.KotlinLogging
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.data.requery.kotlin.domain.AbstractDomainTest
import org.springframework.data.requery.kotlin.domain.hierarchy.tree.NodeAttribute
import org.springframework.data.requery.kotlin.domain.hierarchy.tree.NodeAttributeEntity
import org.springframework.data.requery.kotlin.domain.hierarchy.tree.TreeNode
import org.springframework.data.requery.kotlin.domain.hierarchy.tree.TreeNodeEntity

/**
 * org.springframework.data.requery.kotlin.domain.hierarchy.HierarchyTest
 *
 * @author debop
 */
class HierarchyTest: AbstractDomainTest() {

    companion object {
        private val log = KotlinLogging.logger { }

        fun treeNodeOf(name: String, parent: TreeNode? = null): TreeNodeEntity {

            val node = TreeNodeEntity().also {
                it.name = name
                it.nodePosition.nodeLevel = 0
                it.nodePosition.nodeLevel = 0

            }

            parent?.let {
                parent.children.add(node)
                node.parent = parent
                node.nodePosition.nodeLevel = parent.nodePosition.nodeLevel + 1
            }

            (0 until 2).forEach {
                val attr = nodeAttributeOf()
                node.attributes.add(attr)
                // attr.node = node
            }

            return node
        }

        fun nodeAttributeOf(): NodeAttributeEntity {
            return NodeAttributeEntity().apply {
                name = "name ${rnd.nextInt(1000000)}"
                value = "value ${rnd.nextInt(1000000)}"
            }
        }
    }

    @Before
    fun setup() {
        operations.deleteAll(NodeAttribute::class)
        operations.deleteAll(TreeNode::class)

        assertThat(operations.count(NodeAttribute::class).get().value()).isEqualTo(0)
        assertThat(operations.count(TreeNode::class).get().value()).isEqualTo(0)
    }

    @Test
    fun `insert root node only`() {

        val root = treeNodeOf("root")

        operations.insert(root)
        assertThat(root.id).isNotNull()

        val loadedRoot = operations.findById(TreeNode::class, root.id)!!
        assertThat(loadedRoot).isEqualTo(root)

        operations.delete(loadedRoot)

        assertThat(operations.count(NodeAttribute::class).get().value()).isEqualTo(0)
        assertThat(operations.count(TreeNode::class).get().value()).isEqualTo(0)
    }
}