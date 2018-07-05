package org.springframework.data.requery.domain.sample;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Transient;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.requery.domain.AbstractPersistable;
import org.springframework.data.requery.domain.ToStringBuilder;

@Entity
public abstract class AbstractDummy extends AbstractPersistable<Integer> {

    @Key
    @Generated
    protected Integer id;

    protected String name;

    @Override
    @Transient
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("name", name);
    }

    private static final long serialVersionUID = 1312143861898946917L;
}
