package com.coupang.springframework.data.requery.domain.sample;

import com.coupang.kotlinx.objectx.AbstractValueObject;
import com.coupang.kotlinx.objectx.ValueObject;
import io.requery.Entity;
import io.requery.Key;
import io.requery.ManyToOne;

/**
 * AbstractItemSite
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@Entity
public abstract class AbstractItemSite extends AbstractValueObject {

    @Key
    @ManyToOne
    protected AbstractItem item;

    @Key
    @ManyToOne
    protected AbstractSite site;


    public AbstractItemSite() {}

    public AbstractItemSite(AbstractItem item, AbstractSite site) {
        this.item = item;
        this.site = site;
    }


    private static final long serialVersionUID = -8045801232998470442L;
}
