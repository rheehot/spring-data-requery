package org.springframework.data.requery.domain.sample;

import io.requery.Entity;
import io.requery.Key;
import io.requery.Transient;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.requery.domain.AbstractValueObject;
import org.springframework.data.requery.domain.ToStringBuilder;

import java.io.Serializable;

/**
 * AbstractSampleWithPrimitiveId
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@Getter
@Setter
@Entity
public abstract class AbstractSampleWithPrimitiveId extends AbstractValueObject implements Serializable {

    @Key
    protected long id;

    protected String name;

    @Transient
    @NotNull
    @Override
    protected ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("id", id)
            .add("name", name);
    }

    private static final long serialVersionUID = -1154174706827967903L;
}
