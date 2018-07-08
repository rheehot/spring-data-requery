package org.springframework.data.requery.domain.sample;

import io.requery.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.requery.domain.AbstractAuditable;
import org.springframework.data.requery.domain.ToStringBuilder;

import java.util.Set;

/**
 * AbstractAuditableUser
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@Entity
public abstract class AbstractAuditableUser extends AbstractAuditable<Integer> {

    @Key
    @Generated
    protected Integer id;

    protected String firstname;

    @JunctionTable
    @ManyToMany(cascade = { CascadeAction.DELETE, CascadeAction.SAVE })
    protected Set<AbstractAuditableRole> roles;


    @Override
    @Transient
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("firstname", firstname);
    }

    private static final long serialVersionUID = 7846556717471318510L;
}
