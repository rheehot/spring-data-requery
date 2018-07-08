package org.springframework.data.requery.domain.upsert;

import io.requery.*;
import io.requery.query.MutableResult;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.requery.domain.AbstractPersistable;
import org.springframework.data.requery.domain.ToStringBuilder;

import java.util.UUID;

/**
 * @author Diego on 2018. 6. 10..
 */
@Getter
@Entity(name = "UpsertTag")
@Table(name = "upsert_tag")
public abstract class AbstractUpsertTag extends AbstractPersistable<UUID> {

    @Key
    protected UUID id;

    @Column
    protected String name;

    @ManyToMany(cascade = { CascadeAction.DELETE, CascadeAction.SAVE })
    protected MutableResult<AbstractUpsertEvent> events;

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : System.identityHashCode(this);
    }

    @Transient
    @Override
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("name", name);
    }
}
