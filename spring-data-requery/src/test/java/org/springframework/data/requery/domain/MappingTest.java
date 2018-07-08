package org.springframework.data.requery.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.data.requery.domain.model.Person;
import org.springframework.data.requery.domain.sample.CustomAbstractPersistable;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Diego on 2018. 6. 12..
 */
@Slf4j
public class MappingTest extends AbstractDomainTest {

    @Test
    public void verify_mapping_entities() {
        Person person = new Person();
        person.setName("person");
        requeryTemplate.insert(person);

        assertThat(person.getId()).isNotNull();
    }

    @Test
    public void entry_has_only_id() {
        CustomAbstractPersistable custom = new CustomAbstractPersistable();
        requeryTemplate.insert(custom);

        assertThat(custom.getId()).isNotNull();
    }

}
