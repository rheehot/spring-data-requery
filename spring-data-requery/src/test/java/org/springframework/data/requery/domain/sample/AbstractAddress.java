package org.springframework.data.requery.domain.sample;

import io.requery.Embedded;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.requery.domain.AbstractComponent;

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

    public AbstractAddress() {}

//    public AbstractAddress(String country, String city, String streetName, String streetNo) {
//        this.country = country;
//        this.city = city;
//        this.streetName = streetName;
//        this.streetNo = streetNo;
//    }

}
