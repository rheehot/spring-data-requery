package com.coupang.springframework.data.requery.domain;

import io.requery.*;
import io.requery.converter.LocalDateTimeConverter;

import java.time.LocalDateTime;

/**
 * Auditable 이면서 Identifier 수형이 Integer인 Entity
 *
 * @author debop
 * @since 18. 6. 4
 */
@Superclass
public abstract class AuditableIntEntity extends AbstractPersistable<Integer> {

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

    private static final long serialVersionUID = -4615850184590836079L;
}
