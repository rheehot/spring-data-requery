package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.domain.sample.Role;
import com.coupang.springframework.data.requery.domain.sample.User;
import com.coupang.springframework.data.requery.repository.config.InfrastructureConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * com.coupang.springframework.data.requery.repository.query.RequeryCountQueryCreatorTest
 *
 * @author debop
 * @since 18. 6. 26
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { InfrastructureConfig.class })
public class RequeryCountQueryCreatorTest {

    @Autowired RequeryOperations operations;

    @Test
    public void distinctFlagOnCountQueryIssuesCountDistinct() throws Exception {
        //
    }

    interface SomeRepository extends Repository<User, Integer> {
        void findDistinctByRolesIn(List<Role> roles);
    }
}
