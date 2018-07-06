package org.springframework.data.requery.configs

import io.requery.meta.EntityModel
import io.requery.sql.TableCreationMode
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.requery.domain.sample.Models
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

/**
 * RequeryTestConfiguration
 *
 * @author debop@coupang.com
 */
@Configuration
@EnableTransactionManagement
open class RequeryTestConfiguration: AbstractRequeryConfiguration() {

    companion object {
        private val log = KotlinLogging.logger { }
    }

    override fun getEntityModel(): EntityModel = Models.DEFAULT

    override fun getTableCreationMode(): TableCreationMode = TableCreationMode.CREATE_NOT_EXISTS

    @Bean
    fun dataSource(): DataSource {
        log.info { "Create Datasource for Embedded H2 Database..." }

        return EmbeddedDatabaseBuilder()
            .setName("data")
            .setType(EmbeddedDatabaseType.H2)
            .setScriptEncoding("UTF-8")
            .ignoreFailedDrops(true)
            .build()
    }
}