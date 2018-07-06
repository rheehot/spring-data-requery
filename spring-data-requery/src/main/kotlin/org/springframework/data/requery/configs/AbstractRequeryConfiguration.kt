package org.springframework.data.requery.configs

import io.requery.cache.EmptyEntityCache
import io.requery.meta.EntityModel
import io.requery.sql.ConfigurationBuilder
import io.requery.sql.EntityDataStore
import io.requery.sql.SchemaModifier
import io.requery.sql.TableCreationMode
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.requery.core.RequeryOperations
import org.springframework.data.requery.core.RequeryTemplate
import org.springframework.data.requery.core.RequeryTransactionManager
import org.springframework.data.requery.listeners.LogbackListener
import org.springframework.data.requery.mapping.RequeryMappingContext
import org.springframework.transaction.PlatformTransactionManager
import javax.annotation.PostConstruct
import javax.sql.DataSource

/**
 * AbstractRequeryConfiguration
 *
 * @author debop@coupang.com
 * @since 18. 7. 2
 */
@Configuration
abstract class AbstractRequeryConfiguration {

    companion object {
        private val log = KotlinLogging.logger { }
    }

    @Autowired lateinit var applicationContext: ApplicationContext

    @Autowired lateinit var dataSource: DataSource

    @Bean
    abstract fun getEntityModel(): EntityModel

    fun getTableCreationMode(): TableCreationMode {
        return TableCreationMode.CREATE_NOT_EXISTS
    }

    @Bean
    fun requeryConfiguration(): io.requery.sql.Configuration {
        return ConfigurationBuilder(dataSource, getEntityModel())
            .setEntityCache(EmptyEntityCache())
            .setStatementCacheSize(1024)
            .setBatchUpdateSize(100)
            .addStatementListener(LogbackListener<Any>())
            .build()
    }

    @Bean(destroyMethod = "close")
    fun entityDataStore(): EntityDataStore<Any> {
        return EntityDataStore<Any>(requeryConfiguration()).apply {
            log.info { "Create Requery EntityDataStore instance." }
        }
    }

    @Bean
    fun requeryOperations(): RequeryOperations {
        log.info { "Create RequeryOperations instance." }
        return RequeryTemplate(applicationContext, entityDataStore(), requeryMappingContext())
    }

    fun requeryMappingContext(): RequeryMappingContext {
        return RequeryMappingContext().apply {
            setApplicationContext(applicationContext)
        }
    }

    @Bean
    fun transactionManager(): PlatformTransactionManager {
        return RequeryTransactionManager(entityDataStore(), dataSource)
    }

    /**
     * DB Schema 생성이 필요할 경우, Application 시작 시, 존재하지 않는 TABLE을 생성하도록 해줍니다.
     */
    @PostConstruct
    protected fun buildSchema() {
        log.info { "Create Database Schema..." }
        val schema = SchemaModifier(requeryConfiguration())
        try {
            log.debug { schema.createTablesString(getTableCreationMode()) }
            schema.createTables(getTableCreationMode())
            log.info { "Success to create database schema" }
        } catch(ignored: Exception) {
            log.error(ignored) { "Fail to creation database schema." }
        }
    }
}