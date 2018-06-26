package com.coupang.springframework.data.requery.domain.sample;

import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToMany;
import lombok.Getter;

import java.util.Set;

/**
 * AbstractChild
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@Getter
@Entity
public abstract class AbstractChild extends AbstractPersistable<Long> {

    @Key
    @Generated
    protected Long id;

    @ManyToMany(mappedBy = "children")
    protected Set<Parent> parents;

    protected String name;

    public void addParent(Parent parent) {
        this.getParents().add(parent);
    }

    private static final long serialVersionUID = 4307155877990688374L;
}
