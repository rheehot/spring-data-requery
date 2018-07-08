package org.springframework.data.requery.domain;

import io.requery.sql.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.requery.core.RequeryOperations;
import org.springframework.data.requery.listeners.LogbackListener;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Diego on 2018. 6. 12..
 */
@Slf4j
public class MappingSimpleModelTest {

    private DataSource dataSource;
    private Configuration configuration;
    private EntityDataStore<Object> dataStore;
    private RequeryOperations operations;

    @Before
    public void setup() {
        dataSource = new EmbeddedDatabaseBuilder()
            .setName("test")
            .setType(EmbeddedDatabaseType.H2)
            .setScriptEncoding("UTF-8")
            .ignoreFailedDrops(true)
            .build();

        configuration = new ConfigurationBuilder(dataSource, Models.ADMIN)
            .addStatementListener(new LogbackListener<>())
            .build();

        dataStore = new EntityDataStore<>(configuration);

        SchemaModifier schemaModifier = new SchemaModifier(configuration);

        log.debug("{}", schemaModifier.createTablesString(TableCreationMode.CREATE_NOT_EXISTS));
        schemaModifier.createTables(TableCreationMode.CREATE_NOT_EXISTS);
    }

    @Test
    public void verify_mapping_entities() {
        Admin admin = new Admin();
        admin.setName("person");
        dataStore.insert(admin);

        assertThat(admin.getId()).isNotNull();
    }

}
