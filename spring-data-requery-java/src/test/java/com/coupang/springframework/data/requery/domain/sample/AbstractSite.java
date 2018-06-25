package com.coupang.springframework.data.requery.domain.sample;

import com.coupang.kotlinx.objectx.ToStringBuilder;
import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import org.jetbrains.annotations.NotNull;

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
