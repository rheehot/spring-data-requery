package com.coupang.springframework.data.requery.domain.hierarchy;

import com.coupang.kotlinx.objectx.ToStringBuilder;
import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Getter
@Setter
@Entity
public class AbstractNodeAttribute extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 7424000305448372730L;

    @Key
    @Generated
    @Column(name = "attr_id")
    protected Long id;

    @Column(name = "attr_name")
    protected String name;

    @Column(name = "attr_value")
    protected String value;

    @ManyToOne
    @ForeignKey
    protected AbstractTreeNode node;

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Transient
    @Override
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("name", name)
            .add("value", value);
    }
}
