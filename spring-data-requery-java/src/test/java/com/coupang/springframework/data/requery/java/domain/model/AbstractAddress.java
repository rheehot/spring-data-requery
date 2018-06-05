package com.coupang.springframework.data.requery.java.domain.model;

import io.requery.*;

/**
 * com.coupang.springframework.data.requery.java.domain.model.AbstractBasicLocation
 *
 * @author debop
 * @since 18. 6. 4
 */
@Entity(copyable = true)
public class AbstractAddress extends Coordinate {

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
