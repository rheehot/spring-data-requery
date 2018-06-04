package com.coupang.springframework.data.requery.java.domain.basic;

import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Table;
import lombok.Getter;

import static com.coupang.kotlinx.core.HashxKt.hashOf;

/**
 * com.coupang.springframework.data.requery.java.domain.basic.AbstractKeywords
 *
 * @author debop
 * @since 18. 6. 4
 */
@Getter
@Entity
@Table(name = "basic_keyword")
public class AbstractKeywords extends AbstractPersistable<Integer> {

    private static final long serialVersionUID = -5401099992210202481L;

    @Key
    @Generated
    protected Integer id;

    protected String isNotAJvmKeyword;

    protected Boolean isNew;
    protected Boolean isDefault;
    protected String getAbstract;

}
