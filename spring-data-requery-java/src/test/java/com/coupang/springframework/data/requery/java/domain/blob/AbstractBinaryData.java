package com.coupang.springframework.data.requery.java.domain.blob;

import com.coupang.kotlinx.data.requery.conveters.ByteArrayBlobConverter;
import com.coupang.kotlinx.objectx.ToStringBuilder;
import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.*;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * com.coupang.springframework.data.requery.java.domain.blob.AbstractBinaryData
 *
 * @author debop
 * @since 18. 6. 4
 */
@Getter
@Entity
public abstract class AbstractBinaryData extends AbstractPersistable<Long> {

    private static final long serialVersionUID = -3776835149818493670L;

    @Key
    @Generated
    protected Long id;

    @Column(nullable = false)
    protected String name;

    @Lazy
    @Convert(ByteArrayBlobConverter.class)
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
