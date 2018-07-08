package org.springframework.data.requery.repository.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.requery.domain.sample.Product;
import org.springframework.data.requery.repository.sample.ProductRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * RepositoriesJavaConfigTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 14
 */
@RunWith(SpringRunner.class)
@ContextConfiguration
public class RepositoriesJavaConfigTest {

    @Configuration
    @EnableRequeryRepositories(basePackageClasses = { ProductRepository.class })
    static class TestConfig extends InfrastructureConfig {

        @Autowired ApplicationContext context;

        @Bean
        public Repositories repositories() {
            return new Repositories(context);
        }
    }

    @Autowired Repositories repositories;

    @Test
    public void getTargetRepositoryInstance() {
        assertThat(repositories.hasRepositoryFor(Product.class)).isTrue();
    }
}
