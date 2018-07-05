package org.springframework.data.requery.domain.sample;

import io.requery.Generated;
import io.requery.Key;
import io.requery.Superclass;
import io.requery.Version;
import org.springframework.data.requery.domain.AbstractPersistable;

/**
 * AbstractMappedType
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@Superclass
public abstract class AbstractMappedType extends AbstractPersistable<Long> {

    public AbstractMappedType() {}

    public AbstractMappedType(String attribute1) {
        this.attribute1 = attribute1;
    }

    @Key
    @Generated
    protected Long id;

    @Version
    protected Long version;

    String attribute1;


    private static final long serialVersionUID = 1828035517487100998L;
}
