package com.coupang.springframework.data.requery.domain.sample;

import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import lombok.Getter;
import lombok.Setter;

/**
 * AbstractProduct
 *
 * @author debop@coupang.com
 * @since 18. 6. 14
 */
@Getter
@Setter
@Entity
public abstract class AbstractProduct extends AbstractPersistable<Long> {

    private static final long serialVersionUID = -902572992202733146L;

    @Key
    @Generated
    protected Long id;


    protected String name;

}
