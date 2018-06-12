package com.coupang.springframework.data.requery.java.configs;

import com.coupang.springframework.data.requery.configs.AbstractRequeryConfiguration;
import com.coupang.springframework.data.requery.java.domain.Models;
import com.coupang.springframework.data.requery.repository.RequeryContext;
import com.coupang.springframework.data.requery.repository.support.DefaultRequeryContext;
import io.requery.meta.EntityModel;
import io.requery.sql.EntityDataStore;
import io.requery.sql.TableCreationMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class RequeryTestConfiguration extends AbstractRequeryConfiguration {

    @Override
    @Bean
    public EntityModel getEntityModel() {
        return Models.DEFAULT;
    }

    @Override
    public TableCreationMode getTableCreationMode() {
        return TableCreationMode.CREATE_NOT_EXISTS;
    }

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }


    @Bean
    public RequeryContext requeryContext() {
        Set<EntityDataStore> entityDatastores = new HashSet<>();
        entityDatastores.add(entityDataStore());
        return new DefaultRequeryContext(entityDatastores);
    }
}
