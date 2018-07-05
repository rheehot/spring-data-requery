package org.springframework.data.requery.domain.sample;

import io.requery.Entity;

/**
 * AbstractConcreteType1
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@Entity
public abstract class AbstractConcreteType1 extends AbstractMappedType {

    public AbstractConcreteType1() {}

    public AbstractConcreteType1(String attribute1) {
        this.attribute1 = attribute1;
    }

    private static final long serialVersionUID = -6990849517756084276L;
}
