package com.coupang.springframework.data.requery.java.domain.hierarchy;

import com.coupang.kotlinx.objectx.ToStringBuilder;
import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.*;
import io.requery.query.MutableResult;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * com.coupang.springframework.data.requery.java.domain.hierarchy.AbstractTreeNode
 *
 * @author debop
 * @since 18. 6. 4
 */
@Entity
public class AbstractTreeNode extends AbstractPersistable<Long> {

    private static final long serialVersionUID = -4267441735293906937L;

    @Getter
    @Key
    @Generated
    @Column(name = "nodeId")
    protected Long id;

    @Column(name = "nodeName", length = 48)
    protected String name;

    @ManyToOne
    @ForeignKey(delete = ReferentialAction.SET_NULL, update = ReferentialAction.CASCADE)
    protected TreeNode parent;

    @OneToMany(mappedBy = "parent", cascade = { CascadeAction.DELETE, CascadeAction.SAVE })
    protected MutableResult<TreeNode> children;

    @OneToMany(mappedBy = "node")
    protected MutableResult<NodeAttribute> attributes;

    // TODO: Embedded 로 TreePosition을 넣어야 한다.

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Transient
    @Override
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("name", name);
    }
}
