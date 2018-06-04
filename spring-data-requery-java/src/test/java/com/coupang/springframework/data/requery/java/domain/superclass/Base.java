package com.coupang.springframework.data.requery.java.domain.superclass;

import io.requery.JunctionTable;
import io.requery.Key;
import io.requery.ManyToMany;
import io.requery.Superclass;

import java.util.List;

/**
 * com.coupang.springframework.data.requery.java.domain.superclass.Base
 *
 * @author debop
 * @since 18. 6. 4
 */
@Superclass
public interface Base {

    @Key
    Long getId();

    @ManyToMany
    @JunctionTable
    List<Related> getRelated();
}
