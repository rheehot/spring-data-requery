package org.springframework.data.requery.domain.time;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.data.requery.domain.AbstractDomainTest;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Diego on 2018. 6. 12..
 */
@Slf4j
public class ElementCollectionTest extends AbstractDomainTest {

    @Test
    public void insert_element_collection() {

        assertThatThrownBy(() -> {
            ElementCollectionUser user = new ElementCollectionUser();
            user.setId(UUID.randomUUID());
            user.setName("user");
            Set<String> phoneNumbers = new HashSet<>();
            phoneNumbers.add("555-5555");
            phoneNumbers.add("666-6666");
            user.setPhoneNumbers(phoneNumbers);

            requeryTemplate.insert(user);

            ElementCollectionUser saved = requeryTemplate.select(ElementCollectionUser.class)
                .where(ElementCollectionUser.ID.eq(user.getId()).and(ElementCollectionUser.NAME.eq(user.getName())))
                .get()
                .first();

            assertThat(saved).isNotNull();
            assertThat(saved.getPhoneNumbers()).hasSize(2).containsOnly("555-5555","666-6666");
        }).isInstanceOf(ClassCastException.class);
    }
}
