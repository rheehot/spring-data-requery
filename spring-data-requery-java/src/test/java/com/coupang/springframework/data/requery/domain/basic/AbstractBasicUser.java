package com.coupang.springframework.data.requery.domain.basic;

import com.coupang.kotlinx.objectx.ToStringBuilder;
import com.coupang.springframework.data.requery.domain.AuditableLongEntity;
import io.requery.*;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * AbstractBasicUser
 *
 * @author debop@coupang.com
 * @since 18. 6. 4
 */
@Getter
@Entity(name = "BasicUser", copyable = true)
@Table(name = "basic_user")
public abstract class AbstractBasicUser extends AuditableLongEntity {

    @Key
    @Generated
    protected Long id;

    protected String name;
    protected String email;
    protected LocalDate birthday;
    protected Integer age;

    @ForeignKey
    @OneToOne
    protected AbstractBasicLocation address;

    @ManyToMany(mappedBy = "members")
    protected Set<AbstractBasicGroup> groups;

    protected String about;

    @Column(unique = true)
    protected UUID uuid;

    protected URL homepage;

    protected String picture;

    @Override
    public int hashCode() {
        return Objects.hash(name, email, birthday);
    }

    @Transient
    @Override
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("name", name)
            .add("email", email)
            .add("birthday", birthday);
    }

    private static final long serialVersionUID = -2693264826800934057L;
}
