package org.springframework.data.requery.config;


import io.requery.meta.EntityModel;
import io.requery.sql.EntityDataStore;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.requery.core.RequeryOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * RequeryConfigurationTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 4
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RequeryTestConfiguration.class })
public class RequeryConfigurationTest {

    @Inject
    EntityDataStore dataStore;

    @Inject
    RequeryOperations requeryTemplate;

    @Inject
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
