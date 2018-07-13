package org.springframework.boot.autoconfigure.data.requery;

import io.requery.meta.EntityModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.requery.domain.CityRepository;
import org.springframework.boot.autoconfigure.data.requery.domain.Models;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.requery.repository.config.EnableRequeryRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

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

        @Bean
        @ConditionalOnMissingBean
        public EntityModel getEntityModel() {
            return Models.DEFAULT;
        }
    }

    @Autowired
    private ApplicationContext context;

    @Test
    public void testDefaultRepositoryConfiguration() {

        Arrays.stream(context.getBeanDefinitionNames()).forEach(beanName -> {
            log.debug("bean name={}", beanName);
        });

//        assertThat(context.getBean(PlatformTransactionManager.class)).isNotNull();
//        assertThat(context.getBean(EntityDataStore.class)).isNotNull();
//        assertThat(context.getBean(RequeryOperations.class)).isNotNull();

//        assertThat(context.getBean(CityRepository.class)).isNotNull();
    }


}
