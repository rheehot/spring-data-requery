package com.coupang.springframework.data.requery.domain.sample;

import com.coupang.kotlinx.objectx.AbstractValueObject;
import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.Entity;
import io.requery.Key;
import io.requery.Version;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * AbstractSampleWithTimestampVersion
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@Entity
public abstract class AbstractSampleWithTimestampVersion extends AbstractPersistable<Long> {

    @Key
    protected Long id;

    @Version
    protected Timestamp version;

    protected String name;


    private static final long serialVersionUID = 5772857643638279675L;
}
