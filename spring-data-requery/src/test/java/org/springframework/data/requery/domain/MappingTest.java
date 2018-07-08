package org.springframework.data.requery.domain;

import io.requery.sql.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.requery.core.RequeryOperations;
import org.springframework.data.requery.domain.model.Person;
import org.springframework.data.requery.domain.sample.CustomAbstractPersistable;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/**
 * @author Diego on 2018. 6. 12..
 */
@Slf4j
public class MappingTest {

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

        configuration = new ConfigurationBuilder(dataSource, Models.DEFAULT).build();

        dataStore = new EntityDataStore<>(configuration);

        SchemaModifier schemaModifier = new SchemaModifier(configuration);

        log.debug("{}", schemaModifier.createTablesString(TableCreationMode.CREATE_NOT_EXISTS));
        schemaModifier.createTables(TableCreationMode.CREATE_NOT_EXISTS);
    }

    @Test
    public void verify_mapping_entities() {
        Person person = new Person();
        person.setName("person");
        dataStore.insert(person);

        assertThat(person.getId()).isNotNull();
    }

    @Test
    public void entry_has_only_id() {
        CustomAbstractPersistable custom = new CustomAbstractPersistable();
        dataStore.insert(custom);

        assertThat(custom.getId()).isNotNull();
    }

}
