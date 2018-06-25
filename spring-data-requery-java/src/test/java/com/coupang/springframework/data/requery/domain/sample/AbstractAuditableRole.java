package com.coupang.springframework.data.requery.domain.sample;

import com.coupang.kotlinx.objectx.ToStringBuilder;
import com.coupang.springframework.data.requery.domain.AbstractAuditable;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Transient;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * AbstractAuditableRole
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@Entity
@NoArgsConstructor
public abstract class AbstractAuditableRole extends AbstractAuditable<Long> {

    @Key
    @Generated
    protected Long id;

    protected String name;

    public AbstractAuditableRole(String name) {
        this.name = name;
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Transient
    @Override
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("name", name);
    }

    private static final long serialVersionUID = -7804943655622745713L;
}
