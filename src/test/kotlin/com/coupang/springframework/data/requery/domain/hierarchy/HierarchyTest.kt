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
                //                it.parent = parent
                //                parent?.children?.add(it)
                //
                //                it.attributes.add(nodeAttributeOf())
                //                it.attributes.add(nodeAttributeOf())
                it.parent = parent
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
        requeryTmpl.deleteAll(NodeAttribute::class.java)
        requeryTmpl.deleteAll(TreeNode::class.java)

        assertThat(requeryTmpl.count(TreeNode::class.java)).isEqualTo(0)
        assertThat(requeryTmpl.count(NodeAttribute::class.java)).isEqualTo(0)
    }

    @Test
    fun `insert root node only`() {

        val root = treeNodeOf("root")
        requeryTmpl.insert(root)
        assertThat(root.id).isGreaterThan(0)

        val loadedRoot = requeryTmpl.findById(TreeNode::class.java, root.id)!!

        assertThat(loadedRoot).isEqualTo(root)

        requeryTmpl.delete(loadedRoot)
        assertThat(requeryTmpl.count(TreeNode::class.java)).isEqualTo(0)
        assertThat(requeryTmpl.count(NodeAttribute::class.java)).isEqualTo(0)
    }

    @Test
    fun `insert hierarchy nodes`() {

        val root = treeNodeOf("root")

        val child1 = treeNodeOf("child1", root)
        val child2 = treeNodeOf("child2", root)

        requeryTmpl.insert(root)
        requeryTmpl.refreshAll(root)
        assertThat(root.id).isGreaterThan(0)
        assertThat(child1.id).isGreaterThan(0)
        assertThat(child2.id).isGreaterThan(0)

        val loadedRoot = requeryTmpl.findById(TreeNode::class.java, root.id)!!

        assertThat(loadedRoot).isEqualTo(root)
        assertThat(loadedRoot.children).hasSize(2).containsOnly(child1, child2)

        // cascade delete
        requeryTmpl.delete(loadedRoot)

        assertThat(requeryTmpl.count(TreeNode::class.java)).isEqualTo(0)
        assertThat(requeryTmpl.count(NodeAttribute::class.java)).isEqualTo(0)
    }

    @Test
    fun `insert 3 generation hiearchy nodes and delete child`() {
        val root = treeNodeOf("root")

        val child1 = treeNodeOf("child1", root)
        val child2 = treeNodeOf("child2", root)

        val grandChild11 = treeNodeOf("grandChild11", child1)
        val grandChild12 = treeNodeOf("grandChild12", child1)

        val grandChild21 = treeNodeOf("grandChild21", child2)


        requeryTmpl.insert(root)
        assertThat(root.id).isGreaterThan(0)
        assertThat(child1.id).isGreaterThan(0)
        assertThat(child2.id).isGreaterThan(0)

        val loadedRoot = requeryTmpl.findById(TreeNode::class.java, root.id)!!

        assertThat(loadedRoot).isEqualTo(root)
        assertThat(loadedRoot.children).hasSize(2).containsOnly(child1, child2)

        val childLoaded = requeryTmpl.findById(TreeNode::class.java, child1.id)!!
        assertThat(childLoaded.children).hasSize(2).containsOnly(grandChild11, grandChild12)

        // child delete
        requeryTmpl.delete(childLoaded)

        // HINT: child가 삭제된 후에는 parent를 refresh 해야 child가 삭제되었음을 안다 (그 전에 parent 에서 child를 제거해되 된다)
        requeryTmpl.refreshAll(loadedRoot)
        assertThat(loadedRoot.children).hasSize(1).containsOnly(child2)

        // cascade delete
        requeryTmpl.delete(loadedRoot)

        assertThat(requeryTmpl.count(TreeNode::class.java)).isEqualTo(0)
        assertThat(requeryTmpl.count(NodeAttribute::class.java)).isEqualTo(0)
    }
}