package com.coupang.springframework.data.requery.domain.model;

import com.coupang.kotlinx.objectx.ToStringBuilder;
import com.coupang.springframework.data.requery.core.EntityState;
import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.*;
import io.requery.converter.LocalDateConverter;
import io.requery.query.MutableResult;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.coupang.kotlinx.core.HashxKt.hashOf;

/**
 * com.coupang.springframework.data.requery.domain.model.AbstractPerson
 *
 * @author debop
 * @since 18. 6. 4
 */
@Getter
@Entity
public class AbstractPerson extends AbstractPersistable<Long> {

    private static final long serialVersionUID = -2176683616253797032L;

    @Key
    @Generated
    @Column(name = "personId")
    protected Long id;

    @Index(value = "idx_person_name_email")
    protected String name;

    @Index(value = "idx_person_name_email")
    protected String email;

    @Convert(LocalDateConverter.class)
    protected LocalDate birthday;

    @Column(value = "'empty'")
    protected String description;

    @Nullable
    protected Integer age;

    @ForeignKey
    @OneToOne(cascade = { CascadeAction.DELETE, CascadeAction.SAVE })
    protected AbstractAddress address;

    @OneToMany(mappedBy = "owner", cascade = { CascadeAction.DELETE, CascadeAction.SAVE })
    protected MutableResult<AbstractPhone> phoneNumbers;

    @OneToMany
    protected Set<AbstractPhone> phoneNumberSet;

    @OneToMany
    protected List<AbstractPhone> phoneNumberList;

    @ManyToMany(mappedBy = "members")
    protected MutableResult<AbstractGroup> groups;

    @ManyToMany(mappedBy = "owners")
    protected MutableResult<AbstractGroup> ownedGroups;

    @JunctionTable(name = "Person_Friends")
    @ManyToMany(mappedBy = "personId")
    protected Set<AbstractPerson> friends;

    @Lazy
    protected String about;

    @Column(unique = true)
    @Naming(getter = "getUUID", setter = "setUUID")
    protected UUID uuid;

    protected URL homepage;

    protected String picture;

    @Override
    public int hashCode() {
        return hashOf(name, email);
    }

    @Transient
    @Override
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("name", name)
            .add("email", email);
    }

    @Getter
    private EntityState previous;

    @Getter
    private EntityState current;

    private void setState(EntityState state) {
        this.previous = current;
        this.current = state;
    }

    @PreInsert
    public void onPreInsert() {
        setState(EntityState.PRE_SAVE);
    }

    @PostInsert
    public void onPostInsert() {
        setState(EntityState.POST_SAVE);
    }

    @PostLoad
    public void onPostLoad() {
        setState(EntityState.POST_LOAD);
    }

    @PreUpdate
    public void onPreUpdate() {
        setState(EntityState.PRE_UPDATE);
    }

    @PostUpdate
    public void onPostUpdate() {
        setState(EntityState.POST_UPDATE);
    }

    @PreDelete
    public void onPreDelete() {
        setState(EntityState.PRE_DELETE);
    }

    @PostDelete
    public void onPostDelete() {
        setState(EntityState.POST_DELETE);
    }
}
