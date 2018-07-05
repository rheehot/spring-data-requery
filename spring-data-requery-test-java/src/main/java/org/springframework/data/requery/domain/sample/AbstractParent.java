package org.springframework.data.requery.domain.sample;

import io.requery.*;
import lombok.Getter;
import org.springframework.data.requery.domain.AbstractPersistable;

import java.util.Set;

/**
 * AbstractParent
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@Getter
@Entity
public abstract class AbstractParent extends AbstractPersistable<Long> {

    @Key
    @Generated
    protected Long id;

    @JunctionTable
    @ManyToMany
    protected Set<AbstractChild> children;

    protected String name;

    public AbstractParent addChild(AbstractChild child) {
        this.getChildren().add(child);
        return this;
    }

    private static final long serialVersionUID = 7310565121245237400L;
}
