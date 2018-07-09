package org.springframework.data.jpa.benchmark;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.benchmark.model.FullLog;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * JpaUtils
 *
 * @author debop@coupang.com
 */
@UtilityClass
public class JpaUtils {

    private static DataSource getDataSource() {
        return new EmbeddedDatabaseBuilder()
            .setName("jpa-benchmark")
            .setType(EmbeddedDatabaseType.H2)
            .ignoreFailedDrops(true)
            .build();
    }

    private static JpaVendorAdapter getJpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setGenerateDdl(true);
        return adapter;
    }

    public static EntityManagerFactory getEntityManagerFactory() {

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

        factory.setPackagesToScan(FullLog.class.getPackage().getName());
        factory.setJpaVendorAdapter(getJpaVendorAdapter());
        factory.setDataSource(getDataSource());

        factory.afterPropertiesSet();

        return factory.getObject();
    }
}
