package org.springframework.boot.autoconfigure.data.requery;

import io.requery.sql.EntityDataStore;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.requery.domain.CityRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.requery.core.RequeryOperations;
import org.springframework.data.requery.repository.config.EnableRequeryRepositories;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link RequeryRepositoriesAutoConfiguration}
 *
 * @author debop
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RequeryRepositoriesAutoConfigurationTest.TestConfiguration.class })
@EnableRequeryRepositories(basePackageClasses = { CityRepository.class })
public class RequeryRepositoriesAutoConfigurationTest {

    @Configuration
    @EnableAutoConfiguration
    static class TestConfiguration {
    }


    @Autowired
    private ApplicationContext context;

    @Test
    public void testDefaultRepositoryConfiguration() {

        Arrays.stream(context.getBeanDefinitionNames()).forEach(beanName -> {
            log.debug("bean name={}", beanName);
        });

        assertThat(context.getBean(EntityDataStore.class)).isNotNull();
        assertThat(context.getBean(PlatformTransactionManager.class)).isNotNull();
        assertThat(context.getBean(RequeryOperations.class)).isNotNull();

        assertThat(context.getBean(CityRepository.class)).isNotNull();
    }


}
