package com.coupang.springframework.data.requery.domain.sample;

import com.coupang.kotlinx.data.requery.conveters.ByteArrayBlobConverter;
import com.coupang.kotlinx.objectx.ToStringBuilder;
import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * com.coupang.springframework.data.requery.domain.sample.AbstractUser
 *
 * @author debop
 * @since 18. 6. 14
 */
@Getter
@Setter
@Entity
@Table(name = "SD_User")
public abstract class AbstractUser extends AbstractPersistable<Integer> {

    private static final long serialVersionUID = -2977575626321229838L;

    @Key
    @Generated
    protected Integer id;

    protected String firstname;
    protected String lastname;
    protected int age;
    protected boolean active;

    protected Date createdAt;

    @Column(nullable = false, unique = true)
    protected String emailAddress;

    @JunctionTable
    @ManyToMany
    protected Set<AbstractUser> colleagues;

    @ManyToMany
    protected Set<AbstractRole> roles;

    @ManyToOne
    protected AbstractUser manager;

    @Embedded
    protected AbstractAddress address;

    @Convert(ByteArrayBlobConverter.class)
    protected byte[] binaryData;

    // NOTE: requery에서는 @ElementCollection은 지원하지 않습니다.
//    @ElementCollection
//    protected Set<String> attributes;

    protected Date dateOfBirth;

    public AbstractUser() {
        this(null, null, null);
    }

    public AbstractUser(String firstname, String lastname, String emailAddress, AbstractRole... roles) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailAddress = emailAddress;
        this.active = true;
        this.roles = new HashSet<>(Arrays.asList(roles));

        this.createdAt = new Date();
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname, emailAddress);
    }

    @Transient
    @Override
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("firstname", firstname)
            .add("lastname", lastname)
            .add("emailAddress", emailAddress);
    }
}
