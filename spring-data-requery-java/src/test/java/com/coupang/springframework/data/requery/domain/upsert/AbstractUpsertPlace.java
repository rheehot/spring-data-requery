package com.coupang.springframework.data.requery.domain.upsert;

import com.coupang.kotlinx.objectx.ToStringBuilder;
import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.*;
import io.requery.query.MutableResult;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import static com.coupang.kotlinx.core.HashxKt.hashOf;

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

    @OneToMany(mappedBy = "place", cascade = { CascadeAction.SAVE})
    protected MutableResult<AbstractUpsertEvent> events;

    @Override
    public int hashCode() {
        return hashOf(name);
    }

    @Transient
    @Override
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("name", name);
    }
}
