package org.springframework.data.requery.domain.model2;

import io.requery.Entity;
import io.requery.Key;
import io.requery.ReadOnly;
import io.requery.Table;
import lombok.Getter;
import org.springframework.data.requery.domain.AbstractPersistable;

import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * com.coupang.springframework.data.requery.domain.model2.AbstractBasicUser
 *
 * @author debop
 * @since 18. 6. 4
 */
@Getter
@Entity
@Table(name = "model2_user")
@ReadOnly
public class AbstractModel2User extends AbstractPersistable<UUID> {

    private static final long serialVersionUID = 325822952757166210L;

    @Key
    protected UUID id;

    @Key
    protected String name;

    protected Integer age;

    protected String email;

    protected Set<String> phoneNumbers;

    protected Map<String, String> attributes;

    protected URL homepage;
}
