package org.springframework.data.requery.domain;

import io.requery.Persistable;
import io.requery.Superclass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Abstract Entity class
 *
 * @author debop@coupang.com
 * @since 18. 6. 4
 */
@Superclass
public abstract class AbstractPersistable<ID> extends AbstractValueObject implements Persistable, Serializable {

    @Nullable
    abstract public ID getId();

    @io.requery.Transient
    public boolean isNew() {
        return getId() == null;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (other == null)
            return false;

        if (other instanceof AbstractPersistable) {
            AbstractPersistable that = (AbstractPersistable) other;
            return (isNew() && that.isNew())
                   ? hashCode() == other.hashCode()
                   : Objects.equals(getId(), that.getId());

        }

        return false;
    }

    @Override
    public int hashCode() {
        return (getId() != null) ? getId().hashCode() : System.identityHashCode(this);
    }

    @NotNull
    @Override
    @io.requery.Transient
    protected ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("id", getId());
    }

    private static final long serialVersionUID = 5460350519810267858L;
}
