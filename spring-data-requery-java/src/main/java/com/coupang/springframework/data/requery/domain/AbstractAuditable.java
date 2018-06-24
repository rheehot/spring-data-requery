package com.coupang.springframework.data.requery.domain;

import io.requery.*;
import io.requery.converter.LocalDateTimeConverter;

import java.time.LocalDateTime;

/**
 * Abstract Auditable Entity
 *
 * @author debop@coupang.com
 * @since 18. 6. 4
 */
@Superclass
public abstract class AbstractAuditable<ID> extends AbstractPersistable<ID> {

    private static final long serialVersionUID = 4027911066590384558L;

    @Convert(LocalDateTimeConverter.class)
    protected LocalDateTime createdDate;

    @Lazy
    @Convert(LocalDateTimeConverter.class)
    protected LocalDateTime lastModifiedDate;


    @PreInsert
    protected void onPreInsert() {
        createdDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onPreUpdate() {
        lastModifiedDate = LocalDateTime.now();
    }

}
