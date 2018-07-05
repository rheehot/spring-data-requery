package org.springframework.data.requery.domain.functional;

import io.requery.*;
import lombok.Getter;

/**
 * @author Diego on 2018. 6. 14..
 */
@Getter
@Entity(cacheable = false)
public abstract class AbstractFuncChildOneToManyNoCascade implements Persistable {

    @Key
    protected Long id;

    @Column
    protected String attribute;

    @ManyToOne(cascade = {CascadeAction.NONE})
    @ForeignKey(delete = ReferentialAction.SET_NULL, update = ReferentialAction.RESTRICT)
    protected AbstractFuncParentNoCascade parent;
}
