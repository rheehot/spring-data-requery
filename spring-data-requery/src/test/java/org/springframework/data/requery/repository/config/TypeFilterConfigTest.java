package org.springframework.data.requery.repository.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.requery.repository.sample.basic.BasicLocationRepository;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TypeFilterConfigTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 14
 */
@Slf4j
@ContextConfiguration
public class TypeFilterConfigTest extends AbstractRepositoryConfigTest {

    @Configuration
    @EnableRequeryRepositories(
        basePackages = { "org.springframework.data.requery.**.repository.sample.basic" },
        excludeFilters = { @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { BasicLocationRepository.class }) })
    static class TestConfiguration extends InfrastructureConfig {

    }

    @Override
    public void testContextCreation() {
        assertThat(userRepository).isNotNull();
        assertThat(groupRepository).isNotNull();
        assertThat(locationRepository).isNull();
    }
}
