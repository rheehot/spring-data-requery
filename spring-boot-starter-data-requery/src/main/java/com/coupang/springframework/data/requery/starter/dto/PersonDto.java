package com.coupang.springframework.data.requery.starter.dto;

import com.coupang.kotlinx.objectx.AbstractValueObject;
import com.coupang.kotlinx.objectx.ToStringBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Diego on 2018. 7. 1..
 */

@NoArgsConstructor
@Getter
@Setter
public class PersonDto extends AbstractValueObject {
    @NotNull
    @Override
    protected ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("name", name)
            .add("email", email);
    }

    private String name;
    private String email;

    public static PersonDto of(String name, String email) {
        PersonDto person = new PersonDto();
        person.setName(name);
        person.setEmail(email);

        return person;
    }
}
