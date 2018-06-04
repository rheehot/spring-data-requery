package com.coupang.springframework.data.requery.java.domain.hierarchy;

import com.coupang.springframework.data.requery.java.domain.AbstractDomainTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * com.coupang.springframework.data.requery.java.domain.hierarchy.HierarchyTest
 *
 * @author debop
 * @since 18. 6. 4
 */
@Slf4j
public class HierarchyTest extends AbstractDomainTest {

    private static Random rnd = new Random(System.currentTimeMillis());

    private static TreeNode treeNodeOf(String name) {
        return treeNodeOf(name, null);
    }

    private static TreeNode treeNodeOf(String name, TreeNode parent) {
        TreeNode node = new TreeNode();

        node.setName(name);
        node.setParent(parent);
        if (parent != null) {
            parent.getChildren().add(node);
        }

        node.getAttributes().add(nodeAttributeOf());
        node.getAttributes().add(nodeAttributeOf());

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
        requeryTemplate.delete(NodeAttribute.class);
        requeryTemplate.delete(TreeNode.class);

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

}
