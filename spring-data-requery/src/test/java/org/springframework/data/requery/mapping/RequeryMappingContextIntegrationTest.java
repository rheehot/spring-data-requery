package org.springframework.data.requery.mapping;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.requery.configs.RequeryTestConfiguration;
import org.springframework.data.requery.core.RequeryOperations;
import org.springframework.data.requery.domain.basic.AbstractBasicUser;
import org.springframework.data.requery.repository.config.EnableRequeryRepositories;
import org.springframework.data.requery.repository.sample.basic.BasicUserRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * RequeryMappingContextIntegrationTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 12
 */
@RunWith(SpringRunner.class)
@ContextConfiguration
public class RequeryMappingContextIntegrationTest {

    @Configuration
    @EnableRequeryRepositories(basePackageClasses = { BasicUserRepository.class },
                               includeFilters = @ComponentScan.Filter(value = { BasicUserRepository.class },
                                                                      type = FilterType.ASSIGNABLE_TYPE))
    static class Config extends RequeryTestConfiguration {

    }

    RequeryMappingContext context;

    @Inject BasicUserRepository users;

    @Inject RequeryOperations operations;

    @Before
    public void setup() {
        context = new RequeryMappingContext();
    }

    @Test
    public void setupMappingContextCorrectly() {

        RequeryPersistentEntity<?> entity = context.getRequiredPersistentEntity(AbstractBasicUser.class);
        assertThat(entity).isNotNull();
    }
}
