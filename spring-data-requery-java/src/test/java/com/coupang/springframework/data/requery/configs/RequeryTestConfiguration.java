package com.coupang.springframework.data.requery.configs;

import com.coupang.kotlinx.data.DataSources;
import com.coupang.springframework.data.requery.domain.Models;
import io.requery.meta.EntityModel;
import io.requery.sql.TableCreationMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
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
        return DataSources.ofEmbeddedH2();
    }
}
