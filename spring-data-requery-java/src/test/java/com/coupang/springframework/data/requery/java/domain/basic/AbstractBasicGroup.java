package com.coupang.springframework.data.requery.java.domain.basic;

import com.coupang.kotlinx.data.requery.conveters.ByteArrayBlobConverter;
import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.*;
import io.requery.query.MutableResult;
import lombok.Getter;

/**
 * com.coupang.springframework.data.requery.java.domain.basic.AbstractBasicGroup
 *
 * @author debop
 * @since 18. 6. 4
 */
@Getter
@Entity
@Table(name = "basic_group")
public class AbstractBasicGroup extends AbstractPersistable<Integer> {

    private static final long serialVersionUID = 6213359648179473134L;

    @Key
    @Generated
    protected Integer id;

    @Column(unique = true)
    protected String name;

    protected String description;

    @Convert(ByteArrayBlobConverter.class)
    protected byte[] picture;

    @JunctionTable
    @ManyToMany
    protected MutableResult<AbstractBasicUser> members;
}
