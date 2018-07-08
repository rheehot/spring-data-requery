package org.springframework.data.requery.domain.functional;

import io.requery.*;
import lombok.Getter;

import java.util.List;

/**
 * @author Diego on 2018. 6. 14..
 */
@Getter
@Entity(cacheable = false)
public abstract class AbstractFuncParentNoCascade implements Persistable {

    @Key
    protected Long id;

    @OneToOne(cascade = { CascadeAction.NONE })
    @ForeignKey(delete = ReferentialAction.SET_NULL, update = ReferentialAction.RESTRICT)
    protected AbstractFuncChildOneToOneNoCascade oneToOne;

    @ManyToOne(cascade = { CascadeAction.NONE })
    @ForeignKey(delete = ReferentialAction.SET_NULL, update = ReferentialAction.RESTRICT)
    protected AbstractFuncChildManyToOneNoCascade manyToOne;

    @OneToMany(cascade = { CascadeAction.NONE })
    protected List<AbstractFuncChildOneToManyNoCascade> oneToMany;

    @ManyToMany(cascade = { CascadeAction.NONE })
    @JunctionTable
    protected List<AbstractFuncChildManyToManyNoCascade> manyToMany;
}
