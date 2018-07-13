package org.springframework.boot.autoconfigure.data.requery.domain;

import io.requery.Column;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.requery.domain.AbstractPersistable;
import org.springframework.data.requery.domain.ToStringBuilder;

import java.util.Objects;

/**
 * org.springframework.boot.autoconfigure.data.requery.domain.AbstractCity
 *
 * @author debop
 */
@Getter
@Entity
public class AbstractCity extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 6441380830729259194L;

    @Key
    @Generated
    protected Long id;

    @Column(nullable = false)
    protected String name;

    @Column(nullable = false)
    protected String state;

    @Column(nullable = false)
    protected String country;

    @Column(nullable = false)
    protected String map;

    protected AbstractCity() {}

    protected AbstractCity(String name, String country) {
        this.name = name;
        this.country = country;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, state, country);
    }

    @Override
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("name", name)
            .add("state", state)
            .add("country", country);
    }
}
