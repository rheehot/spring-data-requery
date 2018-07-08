package org.springframework.data.requery.domain.functional;

import io.requery.*;
import lombok.Getter;
import org.springframework.data.requery.domain.AbstractPersistable;

/**
 * @author Diego on 2018. 6. 14..
 */
@Getter
@Entity
public abstract class AbstractFuncParent extends AbstractPersistable<Long> {

    @Key
    @Generated
    protected Long id;

    @Column
    protected String name;

    @ManyToOne(cascade = { CascadeAction.DELETE })
    @ForeignKey(delete = ReferentialAction.SET_NULL, update = ReferentialAction.RESTRICT)
    protected AbstractFuncChild child;
}
