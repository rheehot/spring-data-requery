package com.coupang.springframework.data.requery.domain.hierarchy;

import com.coupang.springframework.data.requery.domain.AbstractDomainTest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;

import javax.validation.constraints.NotNull;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class HierarchyTest extends AbstractDomainTest {

    private static Random rnd = new Random(System.currentTimeMillis());

    private static TreeNode treeNodeOf(@NotNull String name) {
        return treeNodeOf(name, null);
    }

    private static TreeNode treeNodeOf(@NotNull String name, @Nullable TreeNode parent) {
        TreeNode node = new TreeNode();

        node.setName(name);
        node.getNodePosition().setNodeLevel(0);
        node.getNodePosition().setNodeOrder(0);

        if (parent != null) {
            parent.getChildren().add(node);
            node.setParent(parent);
            node.getNodePosition().setNodeLevel(parent.getNodePosition().getNodeLevel() + 1);
        }

        for (int i = 0; i < 2; i++) {
            NodeAttribute nodeAttr = nodeAttributeOf();
            node.getAttributes().add(nodeAttr);
            nodeAttr.setNode(node);
        }

        return node;
    }

    private static NodeAttribute nodeAttributeOf() {
        NodeAttribute attr = new NodeAttribute();
        attr.setName("name " + rnd.nextInt(100000));
        attr.setValue("value " + rnd.nextInt(100000));
        return attr;
    }

    @Before
    public void setup() {
        requeryTemplate.deleteAll(NodeAttribute.class);
        requeryTemplate.deleteAll(TreeNode.class);

        assertThat(requeryTemplate.count(NodeAttribute.class).get().value()).isEqualTo(0);
        assertThat(requeryTemplate.count(TreeNode.class).get().value()).isEqualTo(0);
    }

    @Test
    public void insertRootNodeOnly() {
        TreeNode root = treeNodeOf("root");
        requeryTemplate.insert(root);

        assertThat(root.getId()).isNotNull();

        TreeNode loadedRoot = requeryTemplate.findById(TreeNode.class, root.getId());
        assertThat(loadedRoot).isNotNull();
        assertThat(loadedRoot).isEqualTo(root);

        requeryTemplate.delete(loadedRoot);

        assertThat(requeryTemplate.count(NodeAttribute.class).get().value()).isEqualTo(0);
        assertThat(requeryTemplate.count(TreeNode.class).get().value()).isEqualTo(0);
    }

    @Test
    public void insert_hierarchy_nodes() {
        TreeNode root = treeNodeOf("root");
        TreeNode child1 = treeNodeOf("child1", root);
        TreeNode child2 = treeNodeOf("child2", root);

        requeryTemplate.insert(root);
        requeryTemplate.refreshAll(root);
        assertThat(root.getId()).isNotNull();
        assertThat(child1.getId()).isNotNull();
        assertThat(child2.getId()).isNotNull();

        TreeNode loadedRoot = requeryTemplate.findById(TreeNode.class, root.id);
        assertThat(loadedRoot).isNotNull().isEqualTo(root);
        assertThat(loadedRoot.getChildren()).hasSize(2).containsOnly(child1, child2);

        // cascade delete
        requeryTemplate.delete(loadedRoot);

        assertThat(requeryTemplate.count(TreeNode.class).get().value()).isEqualTo(0);
        assertThat(requeryTemplate.count(NodeAttribute.class).get().value()).isEqualTo(0);
    }

    @Test
    public void insert_3_generation_hierarchy_nodes_and_delete_child() {
        TreeNode root = treeNodeOf("root");
        TreeNode child1 = treeNodeOf("child1", root);
        TreeNode child2 = treeNodeOf("child2", root);

        TreeNode grandChild11 = treeNodeOf("grandChild11", child1);
        TreeNode grandChild12 = treeNodeOf("grandChild12", child1);

        TreeNode grandChild21 = treeNodeOf("grandChild21", child2);

        requeryTemplate.insert(root);

        assertThat(root.getId()).isNotNull();
        assertThat(child1.getId()).isNotNull();
        assertThat(child2.getId()).isNotNull();

        TreeNode loadedRoot = requeryTemplate.findById(TreeNode.class, root.id);
        assertThat(loadedRoot).isNotNull().isEqualTo(root);
        assertThat(loadedRoot.getChildren()).hasSize(2).containsOnly(child1, child2);

        TreeNode loadedChild = requeryTemplate.findById(TreeNode.class, child1.id);
        assertThat(loadedChild.getChildren()).hasSize(2).containsOnly(grandChild11, grandChild12);

        // child delete
        requeryTemplate.delete(loadedChild);

        // HINT: child가 삭제된 후에는 parent를 refresh 해야 child가 삭제되었음을 안다 (그 전에 parent 에서 child를 제거해되 된다)
        requeryTemplate.refreshAll(loadedRoot);
        assertThat(loadedRoot.getChildren()).hasSize(1).containsOnly(child2);

        // cascade delete
        requeryTemplate.delete(loadedRoot);

        assertThat(requeryTemplate.count(TreeNode.class).get().value()).isEqualTo(0);
        assertThat(requeryTemplate.count(NodeAttribute.class).get().value()).isEqualTo(0);
    }
}
