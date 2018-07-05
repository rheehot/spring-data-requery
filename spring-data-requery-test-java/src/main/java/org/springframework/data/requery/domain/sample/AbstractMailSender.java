package org.springframework.data.requery.domain.sample;

import io.requery.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.requery.domain.AbstractPersistable;
import org.springframework.data.requery.domain.ToStringBuilder;

import java.util.Objects;

/**
 * AbstractMailSender
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@Entity
public abstract class AbstractMailSender extends AbstractPersistable<Long> {

    @Key
    @Generated
    protected Long id;

    protected String name;

    @ForeignKey
    @ManyToOne
    protected AbstractMailUser mailUser;

    public AbstractMailSender() {}

    public AbstractMailSender(String name) {
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

    private static final long serialVersionUID = -6924337437200476666L;
}
