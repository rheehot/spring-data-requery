package com.coupang.springframework.data.requery.repository;

import com.coupang.springframework.data.requery.configs.RequeryTestConfiguration;
import com.coupang.springframework.data.requery.domain.sample.CustomAbstractPersistable;
import com.coupang.springframework.data.requery.repository.config.EnableRequeryRepositories;
import com.coupang.springframework.data.requery.repository.sample.CustomAbstractPersistableRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * com.coupang.springframework.data.requery.repository.AbstractPersistableTest
 *
 * @author debop
 * @since 18. 7. 1
 */
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
