package org.springframework.data.requery.domain.sample;

import io.requery.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.requery.converters.ByteArrayToBlobConverter;
import org.springframework.data.requery.domain.AbstractPersistable;
import org.springframework.data.requery.domain.ToStringBuilder;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

/**
 * // NOTE: @Embedded property 가 있는 Entity에 생성자를 만들지 말라. 기본생성자에서 Embedded component를 생성한다.
 *
 * @author debop
 * @since 18. 6. 14
 */
@Getter
@Setter
@Entity
@Table(name = "SD_User")
public abstract class AbstractUser extends AbstractPersistable<Integer> /*implements UserRepository.RolesAndFirstname*/ {

    private static final long serialVersionUID = -2977575626321229838L;

    @Key
    @Generated
    protected Integer id;

    protected String firstname;
    protected String lastname;
    protected int age;
    protected boolean active;

    protected Timestamp createdAt;

    @Column(nullable = false, unique = true)
    protected String emailAddress;

    @JunctionTable(name = "User_Colleagues")
    @ManyToMany
    protected Set<AbstractUser> colleagues;

    @JunctionTable
    @ManyToMany
    protected Set<AbstractRole> roles;

    @ManyToOne
    protected AbstractUser manager;

    @Getter
    @Embedded
    protected AbstractAddress address;

    @Convert(ByteArrayToBlobConverter.class)
    protected byte[] binaryData;

    // NOTE: requery에서는 @ElementCollection은 지원하지 않습니다.
//    @ElementCollection
//    protected Set<String> attributes;

    protected Date dateOfBirth;

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
