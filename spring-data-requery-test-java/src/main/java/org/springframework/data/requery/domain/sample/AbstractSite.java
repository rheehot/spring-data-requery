package org.springframework.data.requery.domain.sample;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.requery.domain.AbstractPersistable;
import org.springframework.data.requery.domain.ToStringBuilder;

/**
 * AbstractSite
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@Entity
public abstract class AbstractSite extends AbstractPersistable<Integer> {

    @Key
    @Generated
    protected Integer id;

    protected String name;


    @Override
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("name", name);
    }

    private static final long serialVersionUID = -691506293515552942L;
}
