package com.coupang.springframework.data.requery.java.repository.config;

import com.coupang.springframework.data.requery.configs.AbstractRequeryConfiguration;
import com.coupang.springframework.data.requery.java.domain.Models;
import io.requery.meta.EntityModel;
import io.requery.sql.TableCreationMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * InfrastructureConfig
 *
 * @author debop@coupang.com
 * @since 18. 6. 12
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class InfrastructureConfig extends AbstractRequeryConfiguration {

    @Override
    public EntityModel getEntityModel() {
        return Models.DEFAULT;
    }

    @Override
    public TableCreationMode getTableCreationMode() {
        return TableCreationMode.DROP_CREATE;
    }

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setName("data")
            .setType(EmbeddedDatabaseType.H2)
            .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
