package com.coupang.springframework.data.requery.java.domain.superclass;

import io.requery.Entity;
import io.requery.Key;

/**
 * com.coupang.springframework.data.requery.java.domain.superclass.Related
 *
 * @author debop
 * @since 18. 6. 4
 */
@Entity
public interface Related {

    @Key
    Long getId();

}
