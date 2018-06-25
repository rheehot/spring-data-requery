package com.coupang.springframework.data.requery.domain.sample;

import com.coupang.kotlinx.objectx.AbstractValueObject;
import com.coupang.kotlinx.objectx.ToStringBuilder;
import io.requery.Entity;
import io.requery.Key;
import io.requery.Persistable;
import io.requery.Transient;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * AbstractItem
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@Entity
public abstract class AbstractItem extends AbstractValueObject implements Persistable {

    @Key
    protected Integer id;

    @Key
    protected Integer manufacturerId;

    protected String name;

    public AbstractItem() {}

    public AbstractItem(Integer id, Integer manufacturerId) {
        this.id = id;
        this.manufacturerId = manufacturerId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, manufacturerId);
    }

    @Transient
    @NotNull
    @Override
    protected ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("id", id)
            .add("manufacturerId", manufacturerId);
    }

    private static final long serialVersionUID = -2369407425600965820L;
}
