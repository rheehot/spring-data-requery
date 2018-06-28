package com.coupang.springframework.data.requery.domain.hierarchy

import com.coupang.kotlinx.logging.KLogging
import com.coupang.springframework.data.requery.domain.AbstractDomainTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.util.*

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
        with(requeryKotlin) {
            deleteAll(NodeAttribute::class)
            deleteAll(TreeNode::class)

            assertThat(count(TreeNode::class).get().value()).isEqualTo(0)
            assertThat(count(NodeAttribute::class).get().value()).isEqualTo(0)
        }
    }

    @Test
    fun `insert root node only`() {
        with(requeryKotlin) {
            val root = treeNodeOf("root")

            insert(root)
            assertThat(root.id).isGreaterThan(0)

            val loadedRoot = findById(TreeNode::class, root.id)!!
            assertThat(loadedRoot).isEqualTo(root)

            delete(loadedRoot)
            assertThat(count(TreeNode::class).get().value()).isEqualTo(0)
            assertThat(count(NodeAttribute::class).get().value()).isEqualTo(0)
        }
    }

    @Test
    fun `insert hierarchy nodes`() {
        with(requeryKotlin) {
            val root = treeNodeOf("root")

            val child1 = treeNodeOf("child1", root)
            val child2 = treeNodeOf("child2", root)

            insert(root)
            assertThat(root.id).isGreaterThan(0)
            assertThat(child1.id).isGreaterThan(0)
            assertThat(child2.id).isGreaterThan(0)

            val loadedRoot = findById(TreeNode::class, root.id)!!

            assertThat(loadedRoot).isEqualTo(root)
            assertThat(loadedRoot.children).hasSize(2).containsOnly(child1, child2)

            // cascade delete
            delete(loadedRoot)

            assertThat(count(TreeNode::class).get().value()).isEqualTo(0)
            assertThat(count(NodeAttribute::class).get().value()).isEqualTo(0)
        }
    }

    @Test
    fun `insert 3 generation hiearchy nodes and delete child`() {
        with(requeryKotlin) {
            val root = treeNodeOf("root")

            val child1 = treeNodeOf("child1", root)
            val child2 = treeNodeOf("child2", root)

            val grandChild11 = treeNodeOf("grandChild11", child1)
            val grandChild12 = treeNodeOf("grandChild12", child1)

            val grandChild21 = treeNodeOf("grandChild21", child2)


            insert(root)
            insert(grandChild21)
            
            assertThat(root.id).isGreaterThan(0)
            assertThat(child1.id).isGreaterThan(0)
            assertThat(child2.id).isGreaterThan(0)

            val loadedRoot = findById(TreeNode::class, root.id)!!

            assertThat(loadedRoot).isEqualTo(root)
            assertThat(loadedRoot.children).hasSize(2).containsOnly(child1, child2)

            val childLoaded = findById(TreeNode::class, child1.id)!!
            assertThat(childLoaded.children).hasSize(2).containsOnly(grandChild11, grandChild12)

            // child delete
            delete(childLoaded)

            // HINT: child가 삭제된 후에는 parent를 refresh 해야 child가 삭제되었음을 안다 (그 전에 parent 에서 child를 제거해되 된다)
            refreshAll(loadedRoot)
            assertThat(loadedRoot.children).hasSize(1).containsOnly(child2)

            // cascade delete
            delete(loadedRoot)

            assertThat(count(TreeNode::class).get().value()).isEqualTo(0)
            assertThat(count(NodeAttribute::class).get().value()).isEqualTo(0)
        }
    }
}