package com.coupang.springframework.data.requery.domain.sample;

import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.*;

/**
 * AbstractMailMessage
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@Entity
public abstract class AbstractMailMessage extends AbstractPersistable<Long> {

    @Key
    @Generated
    protected Long id;

    @ForeignKey
    @OneToOne
    protected AbstractMailSender mailSender;

    protected String content;

    private static final long serialVersionUID = -6401169833123419204L;
}
