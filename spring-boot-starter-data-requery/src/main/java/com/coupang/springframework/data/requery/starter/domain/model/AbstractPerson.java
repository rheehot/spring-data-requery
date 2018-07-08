package com.coupang.springframework.data.requery.starter.domain.model;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Transient;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.kotlinx.objectx.ToStringBuilder;
import org.springframework.data.requery.kotlin.domain.AbstractPersistable;
import org.springframework.data.requery.starter.dto.PersonDto;

import static org.kotlinx.core.HashxKt.hashOf;

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

    public PersonDto toPersonDto() {
        return PersonDto.of(name, email);
    }
}
