package org.springframework.data.requery.domain.sample;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.requery.domain.AbstractPersistable;
import org.springframework.data.requery.domain.ToStringBuilder;

import java.util.Objects;

/**
 * AbstractMailUser
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@Entity
public abstract class AbstractMailUser extends AbstractPersistable<Long> {

    @Key
    @Generated
    protected Long id;

    protected String name;

    public AbstractMailUser() {}

    public AbstractMailUser(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("name", name);
    }

    private static final long serialVersionUID = 436543193327022652L;
}
