package com.coupang.springframework.data.requery.domain.sample;

import com.coupang.kotlinx.objectx.ToStringBuilder;
import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Table;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * com.coupang.springframework.data.requery.domain.sample.AbstractRole
 *
 * @author debop
 * @since 18. 6. 14
 */
@Getter
@Setter
@Entity
@Table(name = "SD_Roles")
public abstract class AbstractRole extends AbstractPersistable<Integer> {

    private static final long serialVersionUID = -1819301866144058748L;

    @Key
    @Generated
    protected Integer id;

    protected String name;


    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("name", name);
    }
}
