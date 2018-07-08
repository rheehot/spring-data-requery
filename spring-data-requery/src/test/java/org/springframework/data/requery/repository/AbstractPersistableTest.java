package org.springframework.data.requery.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.requery.configs.RequeryTestConfiguration;
import org.springframework.data.requery.domain.sample.CustomAbstractPersistable;
import org.springframework.data.requery.repository.config.EnableRequeryRepositories;
import org.springframework.data.requery.repository.sample.CustomAbstractPersistableRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@RunWith(SpringRunner.class)
@ContextConfiguration
public class AbstractPersistableTest {

    @Configuration
    @EnableRequeryRepositories(basePackageClasses = { CustomAbstractPersistableRepository.class })
    static class TestConfiguration extends RequeryTestConfiguration {
    }

    @Autowired CustomAbstractPersistableRepository repository;

    @Test
    public void shouldBeAbleToSaveAndLoadCustomPersistableWithUuidId() {

        CustomAbstractPersistable entity = new CustomAbstractPersistable();
        CustomAbstractPersistable saved = repository.save(entity);
        CustomAbstractPersistable found = repository.findById(saved.getId()).get();

        assertThat(found).isEqualTo(saved);
    }

    @Test
    public void equalsWorksForProxiedEntities() {

        CustomAbstractPersistable entity = repository.save(new CustomAbstractPersistable());

        CustomAbstractPersistable proxy = repository.getOne(entity.getId());

        assertThat(proxy).isEqualTo(entity);
    }
}
