package org.springframework.data.requery.domain.sample;

import io.requery.Entity;
import io.requery.Key;
import io.requery.Transient;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.requery.domain.AbstractPersistable;
import org.springframework.data.requery.domain.ToStringBuilder;

import java.util.Objects;

/**
 * AbstractCustomer
 *
 * @author debop@coupang.com
 * @since 18. 6. 14
 */
@Getter
@Setter
@Entity
public abstract class AbstractCustomer extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 5109744158340238800L;

    @Key
    protected Long id;

    protected String name;


    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Transient
    @Override
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("name", name);
    }
}
