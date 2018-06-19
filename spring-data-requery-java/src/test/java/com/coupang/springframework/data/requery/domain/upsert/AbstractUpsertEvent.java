package com.coupang.springframework.data.requery.domain.upsert;

import com.coupang.kotlinx.objectx.ToStringBuilder;
import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.requery.*;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

/**
 * @author Diego on 2018. 6. 10..
 */
@Getter
@Entity(name = "UpsertEvent", copyable = true)
@Table(name = "upsert_event")
public abstract class AbstractUpsertEvent extends AbstractPersistable<UUID> {

    @Key
    protected UUID id;

    @JsonProperty("_name")
    protected String name;

    @JsonProperty("_place")
    @ManyToOne
    protected AbstractUpsertPlace place;

    @JsonProperty("_tags")
    @JunctionTable
    @ManyToMany(cascade = {CascadeAction.DELETE, CascadeAction.SAVE})
    protected Set<AbstractUpsertTag> tags;

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : System.identityHashCode(this);
    }

    @Override
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("name", name);
    }
}
