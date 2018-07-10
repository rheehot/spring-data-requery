package org.springframework.data.requery.benchmark;

import io.requery.cache.EmptyEntityCache;
import io.requery.sql.*;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.requery.benchmark.model.Models;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/**
 * RequerySetupUtils
 *
 * @author debop@coupang.com
 */
@UtilityClass
public class RequerySetupUtils {

    private static DataSource dataSource = getDataSource();
    private static Configuration configuration = getConfiguration();
    static EntityDataStore<Object> dataStore = getDataStore();

    @NotNull
    public static DataSource getDataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .setName("requery-benchmark")
            .build();
    }

    @NotNull
    private static Configuration getConfiguration() {
        return new ConfigurationBuilder(dataSource, Models.BENCHMARK)
            .setBatchUpdateSize(100)
            .setStatementCacheSize(1024)
            .setEntityCache(new EmptyEntityCache())
            .build();
    }

    @NotNull
    public static EntityDataStore<Object> getDataStore() {

        SchemaModifier schemaModifier = new SchemaModifier(configuration);
        schemaModifier.createTables(TableCreationMode.CREATE_NOT_EXISTS);

        return new EntityDataStore<>(configuration);
    }
}
