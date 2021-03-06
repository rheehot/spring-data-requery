package org.springframework.data.requery.repository.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

/**
 * RepositoryAutoConfigTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 14
 */
@ContextConfiguration
public class RepositoryAutoConfigTest extends AbstractRepositoryConfigTest {

    @Configuration
    @EnableRequeryRepositories(basePackages = { "org.springframework.data.requery.**.repository.sample.basic" })
    static class TestConfiguration extends InfrastructureConfig {

    }
}
