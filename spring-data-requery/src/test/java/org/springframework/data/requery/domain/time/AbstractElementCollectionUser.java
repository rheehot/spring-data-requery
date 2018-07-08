package org.springframework.data.requery.domain.time;

import io.requery.Entity;
import io.requery.Key;
import io.requery.Persistable;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.requery.domain.AbstractValueObject;
import org.springframework.data.requery.domain.ToStringBuilder;

import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;


/**
 * @author Diego on 2018. 6. 12..
 */
@Getter
@Entity
public abstract class AbstractElementCollectionUser extends AbstractValueObject implements Persistable {

    @Key
    protected UUID id;

    @Key
    protected String name;

    protected Integer age;
    protected String email;

    // HINT: JPA @ElementCollection 과 유사하게 하는 방식은 없다. Set 을 문자열로 한 컬럼에 저장하는 방식이나, OneToMany 를 사용해야 한다.
    //
    protected Set<String> phoneNumbers;
    protected Map<String, String> attributes;

    protected URL homepage;

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @NotNull
    @Override
    protected ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("id", id)
            .add("name", name)
            .add("age", age)
            .add("email", email);
    }
}
