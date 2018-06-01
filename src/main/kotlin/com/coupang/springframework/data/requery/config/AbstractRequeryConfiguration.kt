package com.coupang.springframework.data.requery.config

import com.coupang.kotlinx.data.requery.listeners.LogbackListener
import com.coupang.kotlinx.logging.KLogging
import com.coupang.springframework.data.requery.core.RequeryKotlinTemplate
import com.coupang.springframework.data.requery.core.RequeryTemplate
import com.coupang.springframework.data.requery.mapping.RequeryMappingContext
import io.requery.Persistable
import io.requery.cache.EmptyEntityCache
import io.requery.meta.EntityModel
import io.requery.sql.*
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mapping.model.FieldNamingStrategy
import org.springframework.data.mapping.model.PropertyNameFieldNamingStrategy
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.sql.DataSource

/**
 * AbstractRequeryConfiguration
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
@Configuration
abstract class AbstractRequeryConfiguration {

    companion object: KLogging()

    @Inject lateinit var applicationContext: ApplicationContext
    @Inject lateinit var dataSource: DataSource

    abstract fun getEntityModel(): EntityModel

    abstract fun getTableCreationMode(): TableCreationMode

    @Bean
    fun requeryConfiguration(): io.requery.sql.Configuration {
        return ConfigurationBuilder(dataSource, getEntityModel())
            .useDefaultLogging()
            .setEntityCache(EmptyEntityCache())
            .setStatementCacheSize(1024)
            .setBatchUpdateSize(100)
            .addStatementListener(LogbackListener<Persistable>())
            .build()
            .apply {
                log.debug { "requery configuration=$this" }
            }
    }

    @Bean(destroyMethod = "close")
    fun requeryEntityDataStore(): EntityDataStore<Persistable> {
        return EntityDataStore<Persistable>(requeryConfiguration()).apply {
            log.info { "Create EntityDataStore instance." }
        }
    }

    @Bean(destroyMethod = "close")
    fun requeryKotlinEntityDataStore(): KotlinEntityDataStore<Persistable> {
        return KotlinEntityDataStore<Persistable>(requeryConfiguration()).apply {
            log.info { "Create KotlinEntityDataStore instance." }
        }
    }

    @Bean
    fun requeryTemplate(): RequeryTemplate {
        return RequeryTemplate(requeryEntityDataStore()).apply {
            log.info { "Create RequeryTemplate instance." }
        }
    }

    @Bean
    fun requeryKotlinTemplate(): RequeryKotlinTemplate {
        return RequeryKotlinTemplate(requeryKotlinEntityDataStore()).apply {
            log.info { "Create RequeryKotlinTemplate instance." }
        }
    }

    @Bean
    fun requeryMappingContext(): RequeryMappingContext {
        return RequeryMappingContext().apply {
            setApplicationContext(applicationContext)
            setFieldNamingStrategy(fieldNamingStrategy())
        }
    }

    protected fun fieldNamingStrategy(): FieldNamingStrategy = PropertyNameFieldNamingStrategy.INSTANCE


    @PostConstruct
    fun setupSchema() {
        log.info { "Setup Requery Database Schema ..." }

        val schema = SchemaModifier(requeryConfiguration())

        try {
            log.debug { schema.createTablesString(getTableCreationMode()) }
            schema.createTables(getTableCreationMode())
            log.info { "Success to setup database schema!!!" }
        } catch(ignored: Exception) {
            log.error(ignored) { "Fail to setup database schema !!!" }
        }
    }
}