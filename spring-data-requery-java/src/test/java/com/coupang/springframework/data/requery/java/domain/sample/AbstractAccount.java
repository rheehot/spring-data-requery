package com.coupang.springframework.data.requery.java.domain.sample;

import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.Column;
import io.requery.Entity;
import io.requery.Key;
import lombok.Getter;
import lombok.Setter;

/**
 * AbstractAccount
 *
 * @author debop@coupang.com
 * @since 18. 6. 14
 */
@Getter
@Setter
@Entity(name = "Accounts")
public class AbstractAccount extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 3616603959214418196L;

    @Key
    protected Long id;


    @Column(name = "account_name")
    protected String name;


}
