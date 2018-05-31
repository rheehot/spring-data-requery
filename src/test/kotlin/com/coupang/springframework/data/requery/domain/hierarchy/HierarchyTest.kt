package com.coupang.springframework.data.requery.domain.hierarchy

import com.coupang.kotlinx.logging.KLogging
import com.coupang.springframework.data.requery.domain.AbstractDomainTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.util.*

/**
 * HierarchyTest
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
class HierarchyTest: AbstractDomainTest() {

    companion object: KLogging() {

        val rnd = Random(System.currentTimeMillis())

        fun treeNodeOf(name: String, parent: AbstractTreeNode? = null): AbstractTreeNode {
            return TreeNode().also {
                it.name = name
                parent?.addChild(it)

                it.addAttribute(nodeAttributeOf())
                it.addAttribute(nodeAttributeOf())
            }
        }

        fun nodeAttributeOf(): AbstractNodeAttribute {
            return NodeAttribute().apply {
                name = "name ${rnd.nextInt(100000)}"
                value = "value ${rnd.nextInt(100000)}"
            }
        }
    }

    @Before
    fun `cleanup tree node`() {
        requeryTemplate.deleteAll(TreeNode::class.java)
        assertThat(requeryTemplate.count(TreeNode::class.java)).isEqualTo(0)
        assertThat(requeryTemplate.count(NodeAttribute::class.java)).isEqualTo(0)
    }

    @Test
    fun `insert root node only`() {

        val root = treeNodeOf("root")
        requeryTemplate.insert(root)
        assertThat(root.id).isGreaterThan(0)

        val loadedRoot = requeryTemplate.findById(TreeNode::class.java, root.id)!!

        assertThat(loadedRoot).isEqualTo(root)

        requeryTemplate.delete(loadedRoot)
        assertThat(requeryTemplate.count(TreeNode::class.java)).isEqualTo(0)
        assertThat(requeryTemplate.count(NodeAttribute::class.java)).isEqualTo(0)
    }

    @Test
    fun `insert hierarchy nodes`() {

        val root = treeNodeOf("root")

        val child1 = treeNodeOf("child1", root)
        val child2 = treeNodeOf("child2", root)

        requeryTemplate.insert(root)
        assertThat(root.id).isGreaterThan(0)
        assertThat(child1.id).isGreaterThan(0)
        assertThat(child2.id).isGreaterThan(0)

        val loadedRoot = requeryTemplate.findById(TreeNode::class.java, root.id)!!

        assertThat(loadedRoot).isEqualTo(root)
        assertThat(loadedRoot.children).hasSize(2).containsOnly(child1, child2)

        // cascade delete
        requeryTemplate.delete(loadedRoot)

        assertThat(requeryTemplate.count(TreeNode::class.java)).isEqualTo(0)
        assertThat(requeryTemplate.count(NodeAttribute::class.java)).isEqualTo(0)
    }

    @Test
    fun `insert 3 generation hiearchy nodes`() {
        val root = treeNodeOf("root")

        val child1 = treeNodeOf("child1", root)
        val child2 = treeNodeOf("child2", root)

        val grandChild11 = treeNodeOf("grandChild11", child1)
        val grandChild12 = treeNodeOf("grandChild12", child1)

        val grandChild21 = treeNodeOf("grandChild21", child2)


        requeryTemplate.insert(root)
        assertThat(root.id).isGreaterThan(0)
        assertThat(child1.id).isGreaterThan(0)
        assertThat(child2.id).isGreaterThan(0)

        val loadedRoot = requeryTemplate.findById(TreeNode::class.java, root.id)!!

        assertThat(loadedRoot).isEqualTo(root)
        assertThat(loadedRoot.children).hasSize(2).containsOnly(child1, child2)

        // cascade delete
        requeryTemplate.delete(loadedRoot)

        assertThat(requeryTemplate.count(TreeNode::class.java)).isEqualTo(0)
        assertThat(requeryTemplate.count(NodeAttribute::class.java)).isEqualTo(0)
    }
}