package com.coupang.springframework.data.requery.domain;

import io.requery.Convert;
import io.requery.ManyToOne;
import io.requery.PreInsert;
import io.requery.PreUpdate;
import io.requery.converter.LocalDateConverter;
import org.springframework.data.domain.Auditable;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * AbstractAuditable
 *
 * @author debop@coupang.com
 * @since 18. 6. 4
 */
public abstract class AbstractAuditable<U, ID>
    extends AbstractPersistable<ID>
    implements Auditable<U, ID, LocalDateTime> {

    private static final long serialVersionUID = 4027911066590384558L;

    @ManyToOne
    protected U createdBy;

    @Convert(LocalDateConverter.class)
    protected LocalDateTime createdDate;

    @ManyToOne
    protected U lastModifiedBy;

    @Convert(LocalDateConverter.class)
    protected LocalDateTime lastModifiedDate;

    @Override
    public Optional<U> getCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    @Override
    public void setCreatedBy(U createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public Optional<LocalDateTime> getCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    @Override
    public void setCreatedDate(LocalDateTime creationDate) {
        this.createdDate = creationDate;
    }

    @Override
    public Optional<U> getLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    @Override
    public void setLastModifiedBy(U lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public Optional<LocalDateTime> getLastModifiedDate() {
        return Optional.ofNullable(lastModifiedDate);
    }

    @Override
    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @PreInsert
    protected void onPreInsert() {
        createdDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onPreUpdate() {
        lastModifiedDate = LocalDateTime.now();
    }
}
