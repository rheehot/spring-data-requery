package com.coupang.springframework.data.requery.domain.sample;

import io.requery.Entity;

/**
 * AbstractConcreteType2
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@Entity
public abstract class AbstractConcreteType2 extends AbstractMappedType {

    public AbstractConcreteType2() {}

    public AbstractConcreteType2(String attribute1) {
        super(attribute1);
    }

    private static final long serialVersionUID = 6778710401940951022L;


}
