package org.springframework.data.requery.repository.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.requery.mapping.RequeryMappingContext;
import org.springframework.data.requery.repository.sample.basic.BasicGroupRepository;
import org.springframework.data.requery.repository.sample.basic.BasicLocationRepository;
import org.springframework.data.requery.repository.sample.basic.BasicUserRepository;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * AbstractRepositoryConfigTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 12
 */
@RunWith(SpringRunner.class)
public abstract class AbstractRepositoryConfigTest {

    @Autowired(required = false) BasicUserRepository userRepository;
    @Autowired(required = false) BasicGroupRepository groupRepository;
    @Autowired(required = false) BasicLocationRepository locationRepository;

    @Autowired RequeryMappingContext mappingContext;

    @Test
    public void testContextCreation() {
        assertThat(userRepository).isNotNull();
        assertThat(groupRepository).isNotNull();
        assertThat(locationRepository).isNotNull();
    }

    @Test
    public void repositoriesHaveExceptionTranslationApplied() {
        RequeryRepositoriesRegistrarIntegrationTests.assertExceptionTranslationActive(userRepository);
        RequeryRepositoriesRegistrarIntegrationTests.assertExceptionTranslationActive(groupRepository);
        RequeryRepositoriesRegistrarIntegrationTests.assertExceptionTranslationActive(locationRepository);
    }

    @Test
    public void exposesRequeryMappingContext() {
        assertThat(mappingContext).isNotNull();
    }
}
