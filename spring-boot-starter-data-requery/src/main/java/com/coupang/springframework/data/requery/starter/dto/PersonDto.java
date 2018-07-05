package com.coupang.springframework.data.requery.starter.dto;

import org.kotlinx.objectx.AbstractValueObject;
import org.kotlinx.objectx.ToStringBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * @author Diego on 2018. 7. 1..
 */

@NoArgsConstructor
@Data
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
