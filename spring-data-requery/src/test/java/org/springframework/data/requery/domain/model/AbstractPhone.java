package org.springframework.data.requery.domain.model;

import io.requery.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.requery.converters.IntArrayListToStringConverter;
import org.springframework.data.requery.domain.AbstractPersistable;
import org.springframework.data.requery.domain.ToStringBuilder;

import java.util.ArrayList;
import java.util.Objects;

/**
 * org.springframework.data.requery.domain.model.AbstractPhone
 *
 * @author debop
 * @since 18. 6. 4
 */
@Getter
@Entity
@NoArgsConstructor
public abstract class AbstractPhone extends AbstractPersistable<Integer> {

    private static final long serialVersionUID = -3682340011300104110L;

    public AbstractPhone(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public AbstractPhone(String phoneNumber, boolean normalized) {
        this.phoneNumber = phoneNumber;
        this.normalized = normalized;
    }

    @Key
    @Generated
    protected Integer id;

    protected String phoneNumber;
    protected boolean normalized;

    @Column
    @Convert(IntArrayListToStringConverter.class)
    protected ArrayList<Integer> extensions;

    @ManyToOne
    protected AbstractPerson owner;

    @Override
    public int hashCode() {
        return Objects.hash(phoneNumber, normalized);
    }

    @Transient
    @Override
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("phoneNumber", phoneNumber)
            .add("normalized", normalized)
            .add("extensions", extensions);
    }
}
