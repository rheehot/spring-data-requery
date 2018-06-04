package com.coupang.springframework.data.requery.java.domain.model;

import com.coupang.kotlinx.objectx.ToStringBuilder;
import com.coupang.springframework.data.requery.convert.IntArrayStringConverter;
import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.coupang.kotlinx.core.HashxKt.hashOf;

/**
 * com.coupang.springframework.data.requery.java.domain.model.AbstractPhone
 *
 * @author debop
 * @since 18. 6. 4
 */
@Getter
@Entity
@NoArgsConstructor
public class AbstractPhone extends AbstractPersistable<Integer> {

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

    @Convert(IntArrayStringConverter.class)
    protected ArrayList<Integer> extensions;

    @ManyToOne
    protected AbstractPerson owner;

    @Override
    public int hashCode() {
        return hashOf(phoneNumber, normalized);
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
