package org.springframework.data.requery.domain.model;

import io.requery.*;
import lombok.Getter;
import lombok.Setter;

/**
 * org.springframework.data.requery.domain.model.AbstractBasicLocation
 *
 * @author debop
 * @since 18. 6. 4
 */
@Getter
@Setter
@Entity(copyable = true)
public abstract class AbstractAddress extends Coordinate {

    @Key
    @Generated
    protected int id;

    protected String line1;
    protected String line2;

    protected String state;

    @Column(length = 5)
    protected String zip;

    @Column(length = 2)
    protected String countryCode;

    protected String city;

    @OneToOne(mappedBy = "address", cascade = { CascadeAction.DELETE, CascadeAction.SAVE })
    protected AbstractPerson person;

    @Convert(AddressTypeConverter.class)
    protected AddressType type;

    @Override
    public String toString() {
        return "Don't override me";
    }
}
