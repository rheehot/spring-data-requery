package com.coupang.springframework.data.requery.repository;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.domain.sample.Role;
import com.coupang.springframework.data.requery.domain.sample.User;
import com.coupang.springframework.data.requery.repository.config.EnableRequeryRepositories;
import com.coupang.springframework.data.requery.repository.config.InfrastructureConfig;
import com.coupang.springframework.data.requery.repository.sample.RoleRepository;
import com.coupang.springframework.data.requery.repository.sample.UserRepository;
import com.coupang.springframework.data.requery.repository.sample.UserRepositoryImpl;
import com.coupang.springframework.data.requery.repository.support.RequeryRepositoryFactoryBean;
import com.coupang.springframework.data.requery.repository.support.SimpleRequeryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * UserRepositoryTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 28
 */
@RunWith(SpringRunner.class)
@ContextConfiguration
public class UserRepositoryTest {

    @Configuration
    @EnableRequeryRepositories(basePackageClasses = { UserRepository.class })
    static class TestConfiguration extends InfrastructureConfig {

        @Autowired ApplicationContext applicationContext;
        @Autowired RequeryOperations operations;

        @Bean
        public UserRepository userRepository() {

            RequeryRepositoryFactoryBean<UserRepository, User, Integer> factory = new RequeryRepositoryFactoryBean<>(UserRepository.class);

            factory.setOperations(operations);
            factory.setBeanFactory(applicationContext);
            factory.setRepositoryBaseClass(SimpleRequeryRepository.class);

            factory.setCustomImplementation(new UserRepositoryImpl());

            factory.afterPropertiesSet();

            return factory.getObject();
        }

        @Bean
        public RoleRepository roleRepository() {
            RequeryRepositoryFactoryBean<RoleRepository, Role, Integer> factory = new RequeryRepositoryFactoryBean<>(RoleRepository.class);

            return factory.getObject();
        }
    }

    @Autowired RequeryOperations operations;

    // CUT
    @Autowired UserRepository repository;

    // Test fixture
    User firstUser, secondUser, thirdUser, fourthUser;
    Integer id;
    Role adminRole;

    private static User createUser(String firstname, String lastname, String email, Role... roles) {
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmailAddress(email);

        for (Role role : roles) {
            user.getRoles().add(role);
        }
        return user;
    }


    @Before
    public void setup() throws Exception {

        firstUser = createUser("Debop", "Bae", "debop@coupang.com");
        firstUser.setAge(51);

        secondUser = createUser("HyoungGab", "Ahn", "diego@coupang.com");
        secondUser.setAge(30);

        Thread.sleep(10);

        thirdUser = createUser("Jinie", "Park", "jinie@coupang.com");
        thirdUser.setAge(26);

        fourthUser = createUser("Nickoon", "Jeon", "nickoon@coupang.com");
        fourthUser.setAge(30);

        adminRole = new Role("admin");

        repository.deleteAll();
    }

    @Test
    public void testCreation() {

        Integer before = operations.count(User.class).get().value();

        flushTestUsers();

        assertThat(operations.count(User.class).get().value()).isEqualTo(before + 4);
    }

    @Test
    public void testRead() {

        flushTestUsers();

        assertThat(repository.findById(id)).map(User::getFirstname).contains(firstUser.getFirstname());
    }

    @Test
    public void findAllByGivenIds() {
        flushTestUsers();
        assertThat(repository.findAllById(Arrays.asList(firstUser.getId(), secondUser.getId()))).contains(firstUser, secondUser);
    }

    @Test
    public void testReadByIdReturnsNullForNotFoundEntities() {

        flushTestUsers();
        assertThat(repository.findById(-27 * id)).isNotPresent();
    }

    @Test
    public void savesCollectionCorrectly() {
        List<User> savedUsers = repository.saveAll(Arrays.asList(firstUser, secondUser, thirdUser));

        assertThat(savedUsers).hasSize(3).containsOnly(firstUser, secondUser, thirdUser);
        savedUsers.forEach(user -> assertThat(user.getId()).isNotNull());
    }

    @Test
    public void savingEmptyCollectionIsNoOp() {

        assertThat(repository.saveAll(new ArrayList<>())).isEmpty();
    }

    @Test
    public void testUpdate() {

        flushTestUsers();

        User foundPerson = repository.findById(id).get();
        foundPerson.setLastname("Kwon");

        repository.upsert(foundPerson);
        repository.refresh(foundPerson);

        assertThat(repository.findById(id)).map(User::getFirstname).contains(foundPerson.getFirstname());
    }

    @Test
    public void existReturnsWhetherAnEntityCanBeLoaded() {
        flushTestUsers();

        assertThat(repository.existsById(id)).isTrue();
        assertThat(repository.existsById(-27 * id)).isFalse();
    }

    @Test
    public void deletesAUserById() {
        flushTestUsers();

        repository.deleteById(firstUser.getId());
    }

    @Test
    public void testDelete() {
        flushTestUsers();

        repository.delete(firstUser);

        assertThat(repository.existsById(id)).isFalse();
        assertThat(repository.findById(id)).isNotPresent();
    }

    @Test
    public void returnsAllSortedCorrectly() {
        flushTestUsers();

        assertThat(repository.findAll(Sort.by(Sort.Direction.ASC, "lastname")))
            .hasSize(4)
            .containsExactly(secondUser, firstUser, fourthUser, thirdUser);
    }

    @Test
    public void deleteCollectionOfEntities() {
        flushTestUsers();

        long before = repository.count();

        repository.deleteAll(Arrays.asList(firstUser, secondUser));

        assertThat(repository.existsById(firstUser.getId())).isFalse();
        assertThat(repository.existsById(secondUser.getId())).isFalse();

        assertThat(repository.count()).isEqualTo(before - 2);
    }

    @Test
    public void batchDeleteCollectionOfEntities() {
        flushTestUsers();

        long before = repository.count();

        repository.deleteInBatch(Arrays.asList(firstUser, secondUser));

        assertThat(repository.existsById(firstUser.getId())).isFalse();
        assertThat(repository.existsById(secondUser.getId())).isFalse();

        assertThat(repository.count()).isEqualTo(before - 2);
    }

    @Test
    public void deleteEmptyCollectionDoesNotDeleteAnything() {
        assertDeleteCallDoesNotDeleteAnything(new ArrayList<>());
    }

    @Test
    public void executesManipulatingQuery() {
        flushTestUsers();

        repository.renameAllUsersTo("newLastname");

        long expected = repository.count();
        assertThat(repository.findByLastname("newLastname")).hasSize((int) expected);
    }

    @Test
    public void testFinderInvocationWithNullParameter() {

        flushTestUsers();

        repository.findByLastname((String) null);
    }

    protected void flushTestUsers() {

        operations.upsert(adminRole);

        firstUser = repository.save(firstUser);
        secondUser = repository.save(secondUser);
        thirdUser = repository.save(thirdUser);
        fourthUser = repository.save(fourthUser);

        id = firstUser.getId();

        assertThat(id).isNotNull();
        assertThat(secondUser.getId()).isNotNull();
        assertThat(thirdUser.getId()).isNotNull();
        assertThat(fourthUser.getId()).isNotNull();

        assertThat(repository.existsById(id)).isTrue();
        assertThat(repository.existsById(secondUser.getId())).isTrue();
        assertThat(repository.existsById(thirdUser.getId())).isTrue();
        assertThat(repository.existsById(fourthUser.getId())).isTrue();
    }

    private static <T> void assertSameElements(Collection<T> first, Collection<T> second) {
        assertThat(first.size()).isEqualTo(second.size());

        first.forEach(it -> assertThat(second).contains(it));
        second.forEach(it -> assertThat(first).contains(it));
    }

    private void assertDeleteCallDoesNotDeleteAnything(List<User> collection) {

        flushTestUsers();
        long count = repository.count();

        repository.deleteAll(collection);
        assertThat(repository.count()).isEqualTo(count);
    }

}
