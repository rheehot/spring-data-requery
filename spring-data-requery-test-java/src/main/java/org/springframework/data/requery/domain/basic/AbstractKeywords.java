package org.springframework.data.requery.domain.basic;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Table;
import lombok.Getter;
import org.springframework.data.requery.domain.AbstractPersistable;

/**
 * com.coupang.springframework.data.requery.domain.basic.AbstractKeywords
 *
 * @author debop
 * @since 18. 6. 4
 */
@Getter
@Entity
@Table(name = "basic_keyword")
public abstract class AbstractKeywords extends AbstractPersistable<Integer> {

    private static final long serialVersionUID = -5401099992210202481L;

    @Key
    @Generated
    protected Integer id;

    protected String isNotAJvmKeyword;

    protected Boolean isNew;
    protected Boolean isDefault;
    protected String getAbstract;

}
