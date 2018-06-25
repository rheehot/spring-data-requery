package com.coupang.springframework.data.requery.domain.sample;

import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.*;

/**
 * AbstractCategory
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@Entity
public abstract class AbstractCategory extends AbstractPersistable<Long> {

    @Key
    @Generated
    protected Long id;

    @ForeignKey
    @ManyToOne
    protected AbstractProduct product;

    public AbstractCategory() {}

    public AbstractCategory(AbstractProduct product) {
        this.product = product;
    }


    private static final long serialVersionUID = 6433169486936020806L;
}
