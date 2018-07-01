package com.coupang.springframework.data.requery.starter.configs;

import com.coupang.springframework.data.requery.configs.AbstractRequeryConfiguration;
import com.coupang.springframework.data.requery.repository.config.EnableRequeryRepositories;
import com.coupang.springframework.data.requery.starter.domain.model.Models;
import io.requery.meta.EntityModel;
import io.requery.sql.TableCreationMode;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Diego on 2018. 7. 1..
 */

@Configuration
@EnableTransactionManagement
@EnableRequeryRepositories(basePackages = {"com.coupang.springframework.data.requery.starter.domain.repository"})
public class BootRequeryConfiguration extends AbstractRequeryConfiguration {

    @Override
    public EntityModel getEntityModel() {
        return Models.DEFAULT;
    }

    @Override
    public TableCreationMode getTableCreationMode() {
        return TableCreationMode.CREATE_NOT_EXISTS;
    }

}
