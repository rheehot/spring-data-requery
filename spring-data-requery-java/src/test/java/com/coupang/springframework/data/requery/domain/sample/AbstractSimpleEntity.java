package com.coupang.springframework.data.requery.domain.sample;

import com.coupang.kotlinx.objectx.AbstractValueObject;
import com.coupang.kotlinx.objectx.ToStringBuilder;
import io.requery.Entity;
import io.requery.Key;
import io.requery.Transient;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * AbstractSimpleEntity
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@Entity
public abstract class AbstractSimpleEntity extends AbstractValueObject implements Serializable {

    @Key
    protected String first;

    @Key
    protected String second;

    public AbstractSimpleEntity() {}

    public AbstractSimpleEntity(String first, String second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Transient
    @NotNull
    @Override
    protected ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("first", first)
            .add("second", second);
    }

    private static final long serialVersionUID = 2926242852176726572L;
}
