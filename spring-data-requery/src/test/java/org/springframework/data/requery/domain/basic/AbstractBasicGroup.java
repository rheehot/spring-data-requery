package org.springframework.data.requery.domain.basic;

import io.requery.*;
import io.requery.query.MutableResult;
import lombok.Getter;
import org.springframework.data.requery.converters.ByteArrayToBlobConverter;
import org.springframework.data.requery.domain.AbstractPersistable;

/**
 * com.coupang.springframework.data.requery.domain.basic.AbstractBasicGroup
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

    @Convert(ByteArrayToBlobConverter.class)
    protected byte[] picture;

    @JunctionTable
    @ManyToMany
    protected MutableResult<AbstractBasicUser> members;
}
