package org.springframework.data.requery.repository.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.support.PersistenceExceptionTranslationInterceptor;
import org.springframework.data.requery.repository.sample.basic.BasicUserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * RequeryRepositoriesRegistrarIntegrationTests
 *
 * @author debop@coupang.com
 * @since 18. 6. 14
 */
@Slf4j
@RunWith(SpringRunner.class)
@ContextConfiguration
public class RequeryRepositoriesRegistrarIntegrationTests {

    @Autowired BasicUserRepository repository;
    @Autowired SampleRepository sampleRepository;

    @Configuration
    @EnableRequeryRepositories(basePackageClasses = { BasicUserRepository.class })
    static class TestConfig extends InfrastructureConfig {

        @Bean
        public SampleRepository sampleRepository() {
            return new SampleRepository();
        }
    }

    @Test
    public void contextLoading() {
        assertThat(repository).isNotNull();
        assertThat(sampleRepository).isNotNull();
    }

    @Test
    public void doesNotProxyPlainAtRepositoryBeans() {
        assertThat(sampleRepository).isNotNull();
        assertThat(ClassUtils.isCglibProxy(sampleRepository)).isFalse();

        assertExceptionTranslationActive(repository);
    }

    @Repository
    static class SampleRepository {

    }

    public static void assertExceptionTranslationActive(Object repository) {
        if (repository == null)
            return;

        assertThat(repository).isInstanceOf(Advised.class);

        List<Advisor> advisors = Arrays.asList(((Advised) repository).getAdvisors());

        for (Advisor advisor : advisors) {
            log.trace("Advice={}", advisor.getAdvice());
        }

        Boolean matched = advisors
            .stream()
            .anyMatch(advisor -> advisor.getAdvice() instanceof PersistenceExceptionTranslationInterceptor);

        assertThat(matched).isTrue();
    }
}
