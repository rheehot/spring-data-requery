package com.coupang.springframework.data.requery.java.domain.hierarchy;

import com.coupang.kotlinx.objectx.AbstractValueObject;
import com.coupang.kotlinx.objectx.ToStringBuilder;
import io.requery.Embedded;
import io.requery.Transient;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * NodePosition
 *
 * @author debop@coupang.com
 * @since 18. 6. 5
 */
@Getter
@Setter
@Embedded
public class NodePosition extends AbstractValueObject {

    private static final long serialVersionUID = -3405392108976048532L;

    protected int nodeLevel;

    protected int nodeOrder;

    @Override
    public int hashCode() {
        return Objects.hash(nodeLevel * 10000L, nodeOrder);
    }

    @Transient
    @NotNull
    @Override
    protected ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("nodeLevel", nodeLevel)
            .add("nodeOrder", nodeOrder);
    }
}
