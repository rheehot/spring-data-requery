package org.springframework.data.requery.domain.upsert;

import io.requery.*;
import io.requery.query.MutableResult;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.requery.domain.AbstractPersistable;
import org.springframework.data.requery.domain.ToStringBuilder;

import java.util.Objects;

/**
 * @author Diego on 2018. 6. 10..
 */
@Getter
@Entity(name = "UpsertPlace")
@Table(name = "upsert_place")
public abstract class AbstractUpsertPlace extends AbstractPersistable<String> {

    @Key
    protected String id;
    protected String name;

    @OneToMany(mappedBy = "place", cascade = { CascadeAction.SAVE })
    protected MutableResult<AbstractUpsertEvent> events;

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
