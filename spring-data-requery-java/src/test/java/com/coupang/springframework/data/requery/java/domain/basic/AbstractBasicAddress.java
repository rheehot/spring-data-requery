package com.coupang.springframework.data.requery.java.domain.basic;

import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.*;
import lombok.Getter;

/**
 * com.coupang.springframework.data.requery.java.domain.basic.AbstractBasicAddress
 *
 * @author debop
 * @since 18. 6. 4
 */
@Getter
@Entity
@Table(name = "basic_address")
public class AbstractBasicAddress extends AbstractPersistable<Integer> {

    private static final long serialVersionUID = -4665641548712152873L;

    @Key
    @Generated
    protected Integer id;

    protected String line1;
    protected String line2;
    protected String line3;

    @Column(length = 5)
    protected String zip;

    @Column(length = 2)
    protected String countryCode;

    protected String city;

    @OneToOne(mappedBy = "address")
    @Column(name = "basic_user")
    protected AbstractBasicUser user;

    @Transient
    protected String description;
}
