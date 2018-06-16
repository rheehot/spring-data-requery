package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.annotation.Query;
import com.coupang.springframework.data.requery.configs.RequeryTestConfiguration;
import com.coupang.springframework.data.requery.core.RequeryTemplate;
import com.coupang.springframework.data.requery.domain.RandomData;
import com.coupang.springframework.data.requery.domain.basic.BasicUser;
import com.coupang.springframework.data.requery.repository.RequeryRepository;
import com.coupang.springframework.data.requery.repository.support.RequeryRepositoryFactory;
import io.requery.query.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * com.coupang.springframework.data.requery.repository.query.DeclaredRequeryQueryTest
 *
 * @author debop
 * @since 18. 6. 16
 */
@Slf4j
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { RequeryTestConfiguration.class })
public class DeclaredRequeryQueryTest {

    @Inject RequeryTemplate operations;

    DeclaredQueryRepository repository;

    @Before
    public void setup() {
        assertThat(operations).isNotNull();
        repository = new RequeryRepositoryFactory(operations).getRepository(DeclaredQueryRepository.class);

        assertThat(repository).isNotNull();
        repository.deleteAll();
    }

    @Test
    public void singleResultRawQuery() {

        BasicUser user = RandomData.randomUser();
        repository.save(user);

        BasicUser loaded = repository.findByAnnotatedQuery(user.getEmail());

        log.debug("loaded user={}", loaded);
        assertThat(loaded).isNotNull();
    }

    @Test
    public void collectionResultRawQuery() {
        Set<BasicUser> users = RandomData.randomUsers(100);
        repository.saveAll(users);

        assertThat(repository.count()).isGreaterThan(0);

        List<BasicUser> results = repository.findAllByEmailMatches("debop%");
        assertThat(results.size()).isGreaterThan(0);
    }

    @Test
    public void queryWithLimits() {
        Set<BasicUser> users = RandomData.randomUsers(10);
        repository.saveAll(users);

        assertThat(repository.count()).isGreaterThan(0);

        List<BasicUser> results = repository.findWithLimits(5);
        assertThat(results).hasSize(5);
    }

    @Test
    public void multipleParameterQuery() {
        Set<BasicUser> users = RandomData.randomUsers(4);
        repository.saveAll(users);

        BasicUser user = RandomData.randomUser();
        user.setName("배성혁");
        repository.save(user);

        BasicUser loaded = repository.findAllBy(user.getName(), user.getEmail());

        assertThat(loaded).isEqualTo(user);
    }

    @Test
    public void queryTuples() {
        Set<BasicUser> users = RandomData.randomUsers(4);
        repository.saveAll(users);

        BasicUser user = RandomData.randomUser();
        user.setEmail("debop@coupang.com");
        repository.save(user);

        List<Tuple> loaded = repository.findAllIds(user.getEmail());
        assertThat(loaded).hasSize(1);
        assertThat(loaded.get(0).<Long>get("id")).isEqualTo(user.getId());
        assertThat(loaded.get(0).<String>get("name")).isEqualTo(user.getName());
    }


    @Test
    public void queryByLocalData() {

        BasicUser user = RandomData.randomUser();
        repository.save(user);

        List<BasicUser> loaded = repository.findByBirthday(user.getBirthday());
        assertThat(loaded).hasSize(1);
        assertThat(loaded.get(0)).isEqualTo(user);

        List<BasicUser> notexists = repository.findByBirthday(LocalDate.ofEpochDay(0));
        assertThat(notexists).isEmpty();
    }

    interface DeclaredQueryRepository extends RequeryRepository<BasicUser, Long> {

        @Query("select * from basic_user u where u.email = ?")
        BasicUser findByAnnotatedQuery(String email);

        @Query("select * from basic_user u where u.email like ?")
        List<BasicUser> findAllByEmailMatches(String email);

        @Query("select * from basic_user u limit ?")
        List<BasicUser> findWithLimits(int limit);

        @Query("select * from basic_user u where u.name=? and u.email=? limit 1")
        BasicUser findAllBy(String name, String email);

        @Query("select u.id, u.name from basic_user u where u.email=?")
        List<Tuple> findAllIds(String email);

        @Query("select * from basic_user u where u.birthday = ?")
        List<BasicUser> findByBirthday(LocalDate birthday);
    }

}
