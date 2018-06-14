package com.coupang.springframework.data.requery.java.domain.sample;

import com.coupang.springframework.data.requery.domain.AbstractComponent;
import io.requery.Embedded;
import lombok.Getter;
import lombok.Setter;

/**
 * AbstractAddress
 *
 * @author debop@coupang.com
 * @since 18. 6. 14
 */
@Getter
@Setter
@Embedded
public class AbstractAddress extends AbstractComponent {

    private static final long serialVersionUID = 6292061477905475831L;

    protected String country;
    protected String city;
    protected String streetName;
    protected String streetNo;

}
