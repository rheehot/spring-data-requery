package org.springframework.data.requery.domain;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Transient;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * org.springframework.data.requery.domain.AbstractAdmin
 *
 * @author debop
 */
@Getter
@Entity(model = "admin")
public class AbstractAdmin extends AbstractPersistable<Integer> {

    @Key
    @Generated
    protected Integer id;

    protected String name;
    protected String email;


    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }

    @Transient
    @Override
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("name", name);
    }

    private static final long serialVersionUID = 4468551579907832887L;
}
