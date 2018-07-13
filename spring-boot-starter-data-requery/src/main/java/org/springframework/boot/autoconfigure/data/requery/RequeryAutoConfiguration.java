package org.springframework.boot.autoconfigure.data.requery;

import io.requery.cache.WeakEntityCache;
import io.requery.meta.EntityModel;
import io.requery.sql.ConfigurationBuilder;
import io.requery.sql.EntityDataStore;
import io.requery.sql.SchemaModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.requery.core.RequeryTransactionManager;
import org.springframework.data.requery.listeners.LogbackListener;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * org.springframework.boot.autoconfigure.data.requery.RequeryAutoConfiguration
 *
 * @author debop
 */
@Configuration
@ConditionalOnBean({ DataSource.class })
@ConditionalOnProperty(prefix = "spring.data.requery", name = "")
@EnableConfigurationProperties(RequeryProperties.class)
@AutoConfigureAfter({ DataSourceAutoConfiguration.class })
public class RequeryAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RequeryAutoConfiguration.class);

    private final RequeryProperties properties;

    public RequeryAutoConfiguration(RequeryProperties properties) {
        this.properties = properties;
    }

//    @Bean
//    @ConditionalOnMissingBean
//    public EntityModel getEntityModel() {
//        String modelName = properties.getModelName();
//        log.debug("Entity model name={}", modelName);
//
//        try {
//            Class<?> clazz = Class.forName(properties.getModelName());
//            return (EntityModel) clazz.newInstance();
//        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
//            throw new IllegalArgumentException("Not found model name. model name=" + properties.getModelName());
//        }
//    }

    @Bean
    @ConditionalOnMissingBean
//    @ConditionalOnBean({ DataSource.class, EntityModel.class })
    public io.requery.sql.Configuration requeryConfiguration(DataSource dataSource, EntityModel entityModel) {
        return new ConfigurationBuilder(dataSource, entityModel)
            .setStatementCacheSize(properties.getStatementCacheSize())
            .setBatchUpdateSize(properties.getBatchUpdateSize())
            .setEntityCache(new WeakEntityCache())
            .addEntityStateListener(new LogbackListener())
            .build();
    }

    @Bean
    @ConditionalOnMissingBean
//    @ConditionalOnBean({ io.requery.sql.Configuration.class })
    public EntityDataStore<Object> entityDataStore(io.requery.sql.Configuration configuration) {
        return new EntityDataStore<>(configuration);
    }

    @Bean
    @ConditionalOnMissingBean
//    @ConditionalOnBean({ DataSource.class, EntityDataStore.class })
    public PlatformTransactionManager platformTransactionManager(EntityDataStore<Object> entityDataStore, DataSource dataSource) {
        return new RequeryTransactionManager(entityDataStore, dataSource);
    }

    @Autowired io.requery.sql.Configuration configuration;

    /**
     * 사용할 Database에 Requery Entity에 해당하는 Schema 를 생성하는 작업을 수행합니다.
     */
    @PostConstruct
    protected void setupSchema() {
        log.info("Setup Requery Database Schema... mode={}", properties.getTableCreationMode());

        try {
            SchemaModifier schema = new SchemaModifier(configuration);
            log.debug(schema.createTablesString(properties.getTableCreationMode()));
            schema.createTables(properties.getTableCreationMode());
            log.info("Success to setup database schema!!!");
        } catch (Exception e) {
            log.error("Fail to setup database schema!!!", e);
            throw new RuntimeException(e);
        }
    }
}
