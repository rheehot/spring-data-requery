package com.coupang.springframework.data.requery.domain.functional;

import io.requery.*;
import lombok.Getter;

import java.util.List;

/**
 * @author Diego on 2018. 6. 14..
 */
@Getter
@Entity(cacheable = false)
public abstract class AbstractFuncChildManyToManyNoCascade implements Persistable {

    @Key
    protected Long id;

    @Column
    protected String attribute;

    @ManyToMany(mappedBy = "manyToMany", cascade = {CascadeAction.NONE})
    protected List<AbstractFuncParentNoCascade> parents;
}
