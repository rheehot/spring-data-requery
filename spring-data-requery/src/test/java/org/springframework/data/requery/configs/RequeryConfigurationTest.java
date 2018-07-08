package org.springframework.data.requery.configs;

import io.requery.meta.EntityModel;
import io.requery.sql.EntityDataStore;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.requery.core.RequeryTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * RequeryConfigurationTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 4
 */
@Slf4j
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { RequeryTestConfiguration.class })
public class RequeryConfigurationTest {

    @Autowired
    EntityDataStore<Object> dataStore;

    @Autowired
    RequeryTemplate requeryTemplate;

    @Autowired
    EntityModel entityModel;

    @Test
    public void loadEntityModel() {
        log.info("EntityMode={}", entityModel);
        assertThat(entityModel).isNotNull();
    }

    @Test
    public void contextLoading() {
        log.debug("Context Loading...");
        assertThat(dataStore).isNotNull();
        assertThat(requeryTemplate).isNotNull();
    }
}
