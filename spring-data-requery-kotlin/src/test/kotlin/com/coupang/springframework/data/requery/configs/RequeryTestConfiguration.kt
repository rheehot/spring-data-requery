package com.coupang.springframework.data.requery.configs

import com.coupang.kotlinx.data.DataSources
import com.coupang.kotlinx.data.JdbcDrivers
import com.coupang.kotlinx.data.config.DatabaseConfigElement
import com.coupang.kotlinx.data.config.DatabaseSetting
import com.coupang.springframework.data.requery.config.AbstractRequeryConfiguration
import com.coupang.springframework.data.requery.domain.Models
import io.requery.meta.EntityModel
import io.requery.sql.TableCreationMode
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

    override fun getTableCreationMode(): TableCreationMode = TableCreationMode.DROP_CREATE

    @Bean
    fun dataSource(): DataSource {
        val h2 = DatabaseSetting(JdbcDrivers.DRIVER_CLASS_H2,
                                 "jdbc:h2:mem:test;MODE=MYSQL;MVCC=TRUE;",
                                 "sa",
                                 "",
                                 DataSources.MAX_POOL_SIZE,
                                 DataSources.MIN_IDLE_SIZE,
                                 DatabaseConfigElement.TEST_QUERY)

        return DataSources.of(h2)
    }


}