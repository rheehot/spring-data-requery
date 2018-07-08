package org.springframework.data.requery.repository.config

import io.requery.meta.EntityModel
import io.requery.sql.TableCreationMode
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.requery.kotlin.configs.AbstractRequeryConfiguration
import org.springframework.data.requery.kotlin.domain.sample.Models
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
class InfrastructureConfig: AbstractRequeryConfiguration() {

    override fun getEntityModel(): EntityModel = Models.DEFAULT

    override fun getTableCreationMode(): TableCreationMode = TableCreationMode.CREATE_NOT_EXISTS

    @Bean
    fun dataSource(): DataSource =
        EmbeddedDatabaseBuilder()
            .setName("config-test")
            .setType(EmbeddedDatabaseType.H2)
            .setScriptEncoding("UTF-8")
            .ignoreFailedDrops(true)
            .build()
}