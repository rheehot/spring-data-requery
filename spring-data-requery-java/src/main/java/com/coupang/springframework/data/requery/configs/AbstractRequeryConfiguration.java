package com.coupang.springframework.data.requery.configs;

import com.coupang.springframework.data.requery.core.RequeryTemplate;
import io.requery.cache.EmptyEntityCache;
import io.requery.meta.EntityModel;
import io.requery.sql.ConfigurationBuilder;
import io.requery.sql.EntityDataStore;
import io.requery.sql.SchemaModifier;
import io.requery.sql.TableCreationMode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.sql.DataSource;

/**
 * AbstractRequeryConfiguration
 *
 * @author debop@coupang.com
 * @since 18. 6. 4
 */
@Configuration
public abstract class AbstractRequeryConfiguration {

    private static final Logger log = LoggerFactory.getLogger(AbstractRequeryConfiguration.class);

    @Inject
    ApplicationContext applicationContext;

    @Inject
    DataSource dataSource;

    abstract public EntityModel getEntityModel();

    abstract public TableCreationMode getTableCreationMode();

    @Bean
    io.requery.sql.Configuration requeryConfiguration() {
        return new ConfigurationBuilder(dataSource, getEntityModel())
            .useDefaultLogging()
            .setEntityCache(new EmptyEntityCache())
            .setStatementCacheSize(1024)
            .setBatchUpdateSize(100)
            .build();
    }

    @Bean
    EntityDataStore entityDataStore() {
        log.info("Create EntityDataStore instance.");
        return new EntityDataStore(requeryConfiguration());
    }

    @Bean
    RequeryTemplate requeryTemplate() {
        log.info("Create RequeryTemplate instance.");
        return new RequeryTemplate(entityDataStore());
    }

    @PostConstruct
    protected void setupSchema() {
        log.info("Setup Requery Database Schema...");

        SchemaModifier schemaModifier = new SchemaModifier(requeryConfiguration());
        try {
            log.debug(schemaModifier.createTablesString(getTableCreationMode()));

            schemaModifier.createTables(getTableCreationMode());
            log.info("Success to setup database schema!!!");
        } catch (Exception e) {
            log.error("Fail to setup database schema!!!", e);
            throw new RuntimeException(e);
        }
    }
}
