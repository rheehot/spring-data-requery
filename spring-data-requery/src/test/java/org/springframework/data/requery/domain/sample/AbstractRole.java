package org.springframework.data.requery.domain.sample;

import io.requery.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.requery.domain.AbstractPersistable;
import org.springframework.data.requery.domain.ToStringBuilder;

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

    public AbstractRole() {}

    public AbstractRole(String name) {
        this.name = name;
    }

    @Key
    @Generated
    protected Integer id;

    protected String name;


    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Transient
    @Override
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("name", name);
    }
}
