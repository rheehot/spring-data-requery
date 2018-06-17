package com.coupang.springframework.data.requery.configs


import com.coupang.springframework.data.requery.core.KotlinRequeryTemplate
import io.requery.sql.KotlinEntityDataStore
import org.springframework.context.annotation.Bean

/**
 * AbstractKotlinRequeryConfiguration
 *
 * @author debop@coupang.com
 * @since 18. 6. 5
 */
abstract class AbstractKotlinRequeryConfiguration: AbstractRequeryConfiguration() {

    @Bean
    fun kotlinEntityDataStore(): KotlinEntityDataStore<Any> {
        return KotlinEntityDataStore(requeryConfiguration())
    }

    @Bean
    fun kotlinRequeryTemplare(): KotlinRequeryTemplate {
        return KotlinRequeryTemplate(kotlinEntityDataStore())
    }
}