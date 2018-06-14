package com.coupang.springframework.data.requery.domain.model;

import com.coupang.kotlinx.data.requery.conveters.ByteArrayBlobConverter;
import com.coupang.kotlinx.objectx.ToStringBuilder;
import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.*;
import io.requery.query.MutableResult;
import io.requery.query.Order;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

import static com.coupang.kotlinx.core.HashxKt.hashOf;

/**
 * com.coupang.springframework.data.requery.domain.model.AbstractBasicGroup
 *
 * @author debop
 * @since 18. 6. 4
 */
@Slf4j
@Getter
@Entity
@Table(name = "Groups")
public class AbstractGroup extends AbstractPersistable<Long> {

    private static final long serialVersionUID = -4655139079217699310L;

    @Key
    @Generated
    protected Long id;

    protected String name;
    protected String description;
    protected GroupType type;

    @Convert(ByteArrayBlobConverter.class)
    protected byte[] picture;

    @JunctionTable
    @ManyToMany
    protected MutableResult<AbstractPerson> members;

    @JunctionTable(name = "Group_Owners")
    @OrderBy(value = "name", order = Order.ASC)
    @ManyToMany
    protected MutableResult<AbstractPerson> owners;

    protected LocalDateTime createdAt;

    @Transient
    protected String temporaryName;

    @PreInsert
    protected void onPreInsert() {
        createdAt = LocalDateTime.now();
    }

    @PostInsert
    @PostLoad
    @PostUpdate
    protected void onPostEvents() {
        log.debug("Group events. group={}", this);
    }


    @Override
    public int hashCode() {
        return hashOf(name, type);
    }

    @Override
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("name", name)
            .add("type", type);
    }
}
