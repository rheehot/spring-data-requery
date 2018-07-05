package org.springframework.data.requery.domain.stateless;

import io.requery.Entity;
import io.requery.Key;
import io.requery.PreInsert;
import io.requery.Table;
import lombok.Getter;
import org.springframework.data.requery.domain.AbstractPersistable;

import java.time.LocalDateTime;

/**
 * com.coupang.springframework.data.requery.domain.stateless.AbstractEntry
 *
 * @author debop
 * @since 18. 6. 4
 */
@Getter
@Entity(stateless = true)
@Table(name = "stateless_entry")
public abstract class AbstractEntry extends AbstractPersistable<String> {

    private static final long serialVersionUID = -3362956193323919341L;

    @Key
    protected String id;

    protected boolean flag1;
    protected boolean flag2;

    protected LocalDateTime createdAt;

    @PreInsert
    protected void onPreInsert() {
        createdAt = LocalDateTime.now();
    }
}
