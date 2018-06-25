package com.coupang.springframework.data.requery.domain.sample;

import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToMany;

import java.util.Set;

/**
 * AbstractChild
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@Entity
public abstract class AbstractChild extends AbstractPersistable<Long> {

    @Key
    @Generated
    protected Long id;

    @ManyToMany(mappedBy = "children")
    protected Set<AbstractParent> parents;

    public AbstractChild addParent(AbstractParent parent) {
        this.parents.add(parent);

        if (!parent.children.contains(this)) {
            parent.addChild(this);
        }
        return this;
    }


    private static final long serialVersionUID = 4307155877990688374L;
}
