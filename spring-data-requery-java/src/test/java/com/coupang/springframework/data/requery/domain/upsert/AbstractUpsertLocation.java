package com.coupang.springframework.data.requery.domain.upsert;

import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.*;
import lombok.Getter;

import static com.coupang.kotlinx.core.HashxKt.hashOf;

/**
 * @author Diego on 2018. 6. 10..
 */
@Getter
@Entity(name = "UpsertLocation")
@Table(name = "upsert_location")
public abstract class AbstractUpsertLocation extends AbstractPersistable<Integer> {

    @Key
    @Generated
    @Column(name = "locationId")
    protected Integer id;

    @Column
    protected String name;

    @Embedded
    public AbstractUpsertAddress address;

    @Override
    public int hashCode() {
        return hashOf(address);
    }
}
