package com.coupang.springframework.data.requery.domain.functional;

import io.requery.Column;
import io.requery.Entity;
import io.requery.Key;
import io.requery.Persistable;
import lombok.Getter;

/**
 * @author Diego on 2018. 6. 14..
 */
@Getter
@Entity(cacheable = false)
public abstract class AbstractFuncChildOneToOneNoCascade implements Persistable {

    @Key
    protected Long id;

    @Column
    protected String attribute;
}
