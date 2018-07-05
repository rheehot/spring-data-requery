package org.springframework.data.requery.domain.sample;

import io.requery.Column;
import io.requery.Entity;
import io.requery.Key;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.requery.domain.AbstractPersistable;

/**
 * AbstractAccount
 *
 * @author debop@coupang.com
 * @since 18. 6. 14
 */
@Getter
@Setter
@Entity(name = "Accounts")
public abstract class AbstractAccount extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 3616603959214418196L;

    @Key
    protected Long id;


    @Column(name = "account_name")
    protected String name;


}
