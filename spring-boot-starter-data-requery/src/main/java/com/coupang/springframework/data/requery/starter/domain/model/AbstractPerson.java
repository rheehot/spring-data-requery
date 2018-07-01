package com.coupang.springframework.data.requery.starter.domain.model;

import com.coupang.kotlinx.objectx.ToStringBuilder;
import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.*;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import static com.coupang.kotlinx.core.HashxKt.hashOf;

/**
 * @author Diego on 2018. 7. 1..
 */

@Getter
@Entity
public class AbstractPerson extends AbstractPersistable<Long> {

    @Key
    @Generated
    protected Long id;

    protected String name;

    protected String email;

    @Override
    public int hashCode() {
        return hashOf(name, email);
    }

    @Transient
    @Override
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("name", name)
            .add("email", email);
    }
}
