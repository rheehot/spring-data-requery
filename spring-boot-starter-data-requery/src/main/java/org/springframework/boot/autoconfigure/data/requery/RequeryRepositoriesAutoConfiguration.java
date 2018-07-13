package org.springframework.boot.autoconfigure.data.requery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.requery.core.RequeryOperations;
import org.springframework.data.requery.repository.RequeryRepository;
import org.springframework.data.requery.repository.config.RequeryRepositoryConfigurationExtension;
import org.springframework.data.requery.repository.support.RequeryRepositoryFactoryBean;

/**
 * org.springframework.boot.autoconfigure.data.requery.RequeryRepositoriesAutoConfiguration
 *
 * @author debop
 */
@Configuration
@ConditionalOnBean({ RequeryOperations.class })
@ConditionalOnClass({ RequeryRepository.class })
@ConditionalOnMissingBean({ RequeryRepositoryFactoryBean.class, RequeryRepositoryConfigurationExtension.class })
@ConditionalOnProperty(prefix = "spring.data.requery.repositories", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import(RequeryRepositoriesAutoConfigureRgistrar.class)
@AutoConfigureAfter(RequeryDataAutoConfiguration.class)
public class RequeryRepositoriesAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RequeryRepositoriesAutoConfiguration.class);
}
