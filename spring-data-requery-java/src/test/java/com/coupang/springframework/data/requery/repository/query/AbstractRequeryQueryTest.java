package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.domain.sample.User;
import com.coupang.springframework.data.requery.repository.config.InfrastructureConfig;
import org.junit.runner.RunWith;
import org.springframework.data.repository.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.List;

/**
 * com.coupang.springframework.data.requery.repository.query.AbstractRequeryQueryTest
 *
 * @author debop
 * @since 18. 6. 14
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { InfrastructureConfig.class })
public abstract class AbstractRequeryQueryTest {

    @Inject RequeryOperations operations;


    interface SampleRepository extends Repository<User, Integer> {

        List<User> findByLastname(String lastname);

        List<User> findByFirstname(String firstname);

        List<User> findAll();
    }
}