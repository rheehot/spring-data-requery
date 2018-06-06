package com.coupang.springframework.data.requery.java.domain.hierarchy;

import com.coupang.kotlinx.objectx.ToStringBuilder;
import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;

/**
 * com.coupang.springframework.data.requery.java.domain.hierarchy.AbstractTreeNode
 *
 * @author debop
 * @since 18. 6. 4
 */
@Getter
@Setter
@Entity
@Table(name = "tree_node")
public class AbstractTreeNode extends AbstractPersistable<Long> {

    private static final long serialVersionUID = -4267441735293906937L;

    @Key
    @Generated
    @Column(name = "nodeId")
    protected Long id;

    @Column(name = "nodeName", length = 48)
    protected String name;

    @ManyToOne
    @ForeignKey(delete = ReferentialAction.SET_NULL, update = ReferentialAction.CASCADE)
    protected AbstractTreeNode parent;

    @OneToMany(mappedBy = "parent", cascade = { CascadeAction.DELETE, CascadeAction.SAVE })
    protected Set<AbstractTreeNode> children;

    @OneToMany(mappedBy = "node")
    protected Set<NodeAttribute> attributes;

    @Embedded
    protected NodePosition nodePosition;

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Transient
    @Override
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("name", name);
    }
}
