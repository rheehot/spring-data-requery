package org.springframework.data.requery.domain.upsert;

import io.requery.*;
import lombok.Getter;
import org.springframework.data.requery.domain.AbstractPersistable;

import java.util.Objects;

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
        return Objects.hash(address);
    }
}
