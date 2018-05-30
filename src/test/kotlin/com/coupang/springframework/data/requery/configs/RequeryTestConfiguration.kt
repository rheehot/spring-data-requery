package com.coupang.springframework.data.requery.configs

import com.coupang.kotlinx.data.DataSources
import com.coupang.springframework.data.requery.config.AbstractRequeryConfiguration
import com.coupang.springframework.data.requery.domain.sample.Models
import io.requery.meta.EntityModel
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

/**
 * RequeryTestConfiguration
 *
 * @author debop@coupang.com
 * @since 18. 5. 30
 */
@Configuration
class RequeryTestConfiguration: AbstractRequeryConfiguration() {

    override fun getEntityModel(): EntityModel = Models.DEFAULT

    @Bean
    fun dataSource(): DataSource {
        return DataSources.ofEmbeddedH2()
    }

}