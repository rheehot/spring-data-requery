package com.coupang.springframework.data.requery.domain.sample;

import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Table;

/**
 * @author Diego on 2018. 6. 13..
 */
@Entity
@Table(name = "customAbstractPersistable")
public abstract class AbstractCustomAbstractPersistable extends AbstractPersistable<Long> {

    @Key
    @Generated
    protected Long id;

    // NOTE: Id 만 있는 Entity는 Insert 구문이 제대로 생성되지 않습니다.
    protected String attr;

    private static final long serialVersionUID = -1509421819757268522L;
}
