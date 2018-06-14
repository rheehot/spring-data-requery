package com.coupang.springframework.data.requery.repository.support;

import com.coupang.springframework.data.requery.core.RequeryTemplate;
import com.coupang.springframework.data.requery.configs.RequeryTestConfiguration;
import com.coupang.springframework.data.requery.domain.RandomData;
import com.coupang.springframework.data.requery.domain.basic.BasicGroup;
import com.coupang.springframework.data.requery.domain.basic.BasicUser;
import com.coupang.springframework.data.requery.repository.RequeryRepository;
import com.coupang.springframework.data.requery.repository.support.RequeryRepositoryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * RequeryRepositoryTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 12
 */
@Slf4j
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { RequeryTestConfiguration.class })
public class RequeryRepositoryTest {

    @Inject RequeryTemplate operations;

    RequeryRepository<BasicUser, Long> repository;
    CrudRepository<BasicGroup, Integer> groupRepository;

    @Before
    public void setup() {
        assertThat(operations).isNotNull();

        repository = new RequeryRepositoryFactory(operations).getRepository(SampleEntityRepository.class);
        groupRepository = new RequeryRepositoryFactory(operations).getRepository(GroupEntityRepository.class);

        assertThat(repository).isNotNull();
        assertThat(groupRepository).isNotNull();

        repository.deleteAll();
        groupRepository.deleteAll();
    }

    @Test
    public void testCrudOperationsForSimpleEntity() throws Exception {
        BasicUser user = RandomData.randomUser();
        repository.save(user);
        assertThat(repository.existsById(user.getId())).isTrue();
        assertThat(repository.count()).isEqualTo(1L);
        assertThat(repository.findById(user.getId())).isEqualTo(Optional.of(user));

        repository.deleteAll(Arrays.asList(user));
        assertThat(repository.count()).isEqualTo(0L);
    }

    @Test
    public void executesCrudOperationsForEntity() {
        BasicGroup group = RandomData.randomBasicGroup();

        groupRepository.save(group);

        assertThat(group.getId()).isNotNull();
        assertThat(groupRepository.findById(group.getId())).isEqualTo(Optional.of(group));

        groupRepository.delete(group);
        assertThat(repository.count()).isEqualTo(0L);
    }

    @Test
    public void executeMultipleEntities() {
        int userCount = 10;
        Set<BasicUser> users = RandomData.randomUsers(userCount);

        log.debug("Add users... users size={}", users.size());

        repository.saveAll(users);
        assertThat(repository.count()).isEqualTo(userCount);

        int deleted = repository.deleteAllInBatch();
        assertThat(deleted).isEqualTo(userCount);
    }

    private static interface SampleEntityRepository extends RequeryRepository<BasicUser, Long> {

    }

    private static interface GroupEntityRepository extends RequeryRepository<BasicGroup, Integer> {

    }

}
