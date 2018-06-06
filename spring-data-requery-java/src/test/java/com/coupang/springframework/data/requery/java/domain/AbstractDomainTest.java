package com.coupang.springframework.data.requery.java.domain;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.core.RequeryTemplate;
import com.coupang.springframework.data.requery.java.configs.RequeryTestConfiguration;
import io.requery.sql.EntityDataStore;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.Random;

/**
 * com.coupang.springframework.data.requery.java.domain.AbstractDomainTest
 *
 * @author debop
 * @since 18. 6. 4
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RequeryTestConfiguration.class })
public abstract class AbstractDomainTest {

    protected static Random rnd = new Random(System.currentTimeMillis());

    @Inject
    protected EntityDataStore dataStore;

    @Inject
    protected RequeryOperations requeryTemplate;
}
