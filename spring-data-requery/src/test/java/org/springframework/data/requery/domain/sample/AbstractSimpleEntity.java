package org.springframework.data.requery.domain.sample;

import io.requery.Column;
import io.requery.Entity;
import io.requery.Key;
import io.requery.Transient;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.requery.domain.AbstractValueObject;
import org.springframework.data.requery.domain.ToStringBuilder;

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
    @Column(name = "first_key", nullable = false)
    protected String first;

    @Key
    @Column(name = "second_key", nullable = false)
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
