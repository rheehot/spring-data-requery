package org.springframework.data.requery.domain.sample;

import io.requery.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.requery.domain.AbstractPersistable;
import org.springframework.data.requery.domain.ToStringBuilder;

import java.util.Date;
import java.util.Objects;

/**
 * AbstractVersionedUser
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@Entity
public abstract class AbstractVersionedUser extends AbstractPersistable<Long> {

    @Key
    @Generated
    protected Long id;

    @Version
    protected Long version;


    protected String name;

    protected String email;

    protected Date birthday;

    public AbstractVersionedUser() {}

    public AbstractVersionedUser(String name, String email, Date birthday) {
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, birthday);
    }

    @Transient
    @Override
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("name", name)
            .add("email", email)
            .add("birthday", birthday);
    }

    private static final long serialVersionUID = -7900602652543174851L;
}
