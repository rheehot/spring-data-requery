package org.springframework.data.requery.domain.blob;


import io.requery.*;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.requery.converters.ByteArrayToBlobConverter;
import org.springframework.data.requery.domain.AbstractPersistable;
import org.springframework.data.requery.domain.ToStringBuilder;

import java.util.Objects;

/**
 * com.coupang.springframework.data.requery.domain.blob.AbstractBinaryData
 *
 * @author debop
 * @since 18. 6. 4
 */
@Getter
@Entity
public class AbstractBinaryData extends AbstractPersistable<Long> {

    private static final long serialVersionUID = -3776835149818493670L;

    @Key
    @Generated
    protected Long id;

    @Column(nullable = false)
    protected String name;

    @Lazy
    @Convert(ByteArrayToBlobConverter.class)
    protected byte[] picture;

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Transient
    @Override
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("name", name);
    }
}
