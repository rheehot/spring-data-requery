package org.springframework.boot.autoconfigure.data.requery;

import io.requery.sql.EntityDataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.requery.core.RequeryOperations;
import org.springframework.data.requery.core.RequeryTemplate;
import org.springframework.data.requery.mapping.RequeryMappingContext;

import javax.sql.DataSource;

/**
 * org.springframework.boot.autoconfigure.data.requery.RequeryAutoConfiguration
 *
 * @author debop
 */
@Configuration
@ConditionalOnBean({ DataSource.class, EntityDataStore.class })
@AutoConfigureAfter(RequeryAutoConfiguration.class)
public class RequeryDataAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RequeryDataAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(EntityDataStore.class)
    public RequeryOperations requeryOperations(EntityDataStore<Object> entityDataStore,
                                               RequeryMappingContext mappingContext) {
        try {
            return new RequeryTemplate(entityDataStore, mappingContext);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public RequeryMappingContext mappingContext(ApplicationContext applicationContext) {
        RequeryMappingContext mappingContext = new RequeryMappingContext();
        mappingContext.setApplicationContext(applicationContext);
        return mappingContext;
    }


}
