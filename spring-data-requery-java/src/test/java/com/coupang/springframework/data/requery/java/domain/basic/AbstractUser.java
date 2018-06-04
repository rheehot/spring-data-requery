package com.coupang.springframework.data.requery.java.domain.basic;

import com.coupang.kotlinx.objectx.ToStringBuilder;
import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.*;
import org.jetbrains.annotations.NotNull;

import static com.coupang.kotlinx.core.HashxKt.hashOf;

/**
 * AbstractUser
 *
 * @author debop@coupang.com
 * @since 18. 6. 4
 */
@Entity
@Table(name = "Users")
public abstract class AbstractUser extends AbstractPersistable<Long> {

    @Key
    @Generated
    protected Long id;

    protected String name;


    @Override
    public int hashCode() {
        return hashOf(name);
    }

    @Transient
    @Override
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("name", name);
    }

    private static final long serialVersionUID = -2693264826800934057L;
}
