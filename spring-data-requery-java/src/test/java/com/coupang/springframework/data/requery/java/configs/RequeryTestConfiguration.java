package com.coupang.springframework.data.requery.java.configs;

import com.coupang.springframework.data.requery.configs.AbstractRequeryConfiguration;
import com.coupang.springframework.data.requery.java.domain.Models;
import io.requery.meta.EntityModel;
import io.requery.sql.TableCreationMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
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
    DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .build();
    }
}
