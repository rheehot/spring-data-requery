package org.springframework.data.requery.domain.sample;

import io.requery.*;
import org.springframework.data.requery.domain.AbstractPersistable;

/**
 * AbstractOrder
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@Entity
@Table(name = "ORDERS")
public abstract class AbstractOrder extends AbstractPersistable<Long> {

    @Key
    @Generated
    protected Long id;

    @ForeignKey
    @ManyToOne
    protected Customer customer;

    protected String orderNo;


    private static final long serialVersionUID = 3606365166891885684L;
}
