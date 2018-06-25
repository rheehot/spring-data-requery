package com.coupang.springframework.data.requery.domain.sample;

import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.*;

import java.util.Set;

/**
 * AbstractParent
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@Entity
public abstract class AbstractParent extends AbstractPersistable<Long> {

    @Key
    @Generated
    protected Long id;

    @JunctionTable
    @ManyToMany
    protected Set<AbstractChild> children;

    public AbstractParent addChild(AbstractChild child) {
        this.children.add(child);
        if (!child.parents.contains(this)) {
            child.addParent(this);
        }
        return this;
    }

    private static final long serialVersionUID = 7310565121245237400L;
}
