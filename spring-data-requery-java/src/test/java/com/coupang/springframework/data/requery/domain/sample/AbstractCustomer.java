package com.coupang.springframework.data.requery.domain.sample;

import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.Entity;
import io.requery.Key;
import lombok.Getter;
import lombok.Setter;

/**
 * AbstractCustomer
 *
 * @author debop@coupang.com
 * @since 18. 6. 14
 */
@Getter
@Setter
@Entity
public abstract class AbstractCustomer extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 5109744158340238800L;

    @Key
    Long id;
}
