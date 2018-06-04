package com.coupang.springframework.data.requery.java.domain.basic;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Table;

/**
 * AbstractUser
 *
 * @author debop@coupang.com
 * @since 18. 6. 4
 */
@Entity
@Table(name = "Users")
public abstract class AbstractUser {

    @Key
    @Generated
    protected Long id;

    protected String name;
}
