package com.coupang.springframework.data.requery.domain;

import io.requery.*;
import io.requery.converter.LocalDateTimeConverter;

import java.time.LocalDateTime;

/**
 * com.coupang.springframework.data.requery.domain.AuditableLongEntity
 *
 * @author debop
 * @since 18. 6. 4
 */
@Superclass
public abstract class AuditableLongEntity extends AbstractPersistable<Long> {

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

    private static final long serialVersionUID = -6288516556005303756L;
}