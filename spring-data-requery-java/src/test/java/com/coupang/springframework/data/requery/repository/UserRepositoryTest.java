package com.coupang.springframework.data.requery.repository;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.domain.sample.AbstractUser;
import com.coupang.springframework.data.requery.domain.sample.Role;
import com.coupang.springframework.data.requery.domain.sample.User;
import com.coupang.springframework.data.requery.repository.config.EnableRequeryRepositories;
import com.coupang.springframework.data.requery.repository.config.InfrastructureConfig;
import com.coupang.springframework.data.requery.repository.sample.RoleRepository;
import com.coupang.springframework.data.requery.repository.sample.UserRepository;
import com.coupang.springframework.data.requery.repository.sample.UserRepositoryImpl;
import com.coupang.springframework.data.requery.repository.support.RequeryRepositoryFactoryBean;
import com.coupang.springframework.data.requery.repository.support.SimpleRequeryRepository;
import io.requery.query.Result;
import io.requery.query.element.QueryElement;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static com.coupang.springframework.data.requery.utils.RequeryUtils.unwrap;
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

        repository.findByLastname(null);
    }

    @Test
    public void testFindByLastname() {
        flushTestUsers();
        assertThat(repository.findByLastname("Bae")).containsOnly(firstUser);
    }

    @Test
    public void testFindByEmailAddress() {
        flushTestUsers();
        assertThat(repository.findByEmailAddress("debop@coupang.com")).isEqualTo(firstUser);
    }

    @Test
    public void testReadAll() {
        flushTestUsers();

        assertThat(repository.count()).isEqualTo(4L);
        assertThat(repository.findAll()).containsOnly(firstUser, secondUser, thirdUser, fourthUser);
    }

    @Test
    public void deleteAll() {
        flushTestUsers();

        repository.deleteAll();
        assertThat(repository.count()).isZero();
    }

    @Test
    public void deleteAllInBatch() {
        flushTestUsers();

        repository.deleteAllInBatch();
        assertThat(repository.count()).isZero();
    }

    @Test
    public void testCascadesPersisting() {

        // Create link prior to persisting
        firstUser.getColleagues().add(secondUser);

        flushTestUsers();

        User firstReferenceUser = repository.findById(firstUser.getId()).get();
        assertThat(firstReferenceUser).isEqualTo(firstUser);

        Set<AbstractUser> colleagues = firstReferenceUser.getColleagues();
        assertThat(colleagues).containsOnly(secondUser);
    }

    @Test
    public void testPreventsCascadingRolePersisting() {

        firstUser.getRoles().add(new Role("USER"));
        flushTestUsers();
    }

    @Test
    public void testUpsertCascadesCollegues() {

        firstUser.getColleagues().add(secondUser);
        flushTestUsers();

        firstUser.getColleagues().add(createUser("Tao", "Kim", "tagkim@coupang.com"));
        firstUser = repository.upsert(firstUser);

        User reference = repository.findById(firstUser.getId()).get();
        Set<AbstractUser> colleagues = reference.getColleagues();

        assertThat(colleagues).hasSize(2).contains(secondUser);
    }

    @Test
    public void testCountsCorrectly() {

        long count = repository.count();

        User user = createUser("Jane", "Doe", "janedoe@example.com");
        repository.save(user);

        assertThat(repository.count()).isEqualTo(count + 1);
    }

    @Test
    public void testInvocationOfCustomImplementation() {

        repository.someCustomMethod(new User());
    }

    @Test
    public void testOverwritingFinder() {

        repository.findByOverrridingMethod();
    }

    @Test
    public void testUsesQueryAnnotation() {
        assertThat(repository.findByAnnotatedQuery("debop@coupang.com")).isNull();

        flushTestUsers();
        assertThat(repository.findByAnnotatedQuery("debop@coupang.com")).isEqualTo(firstUser);
    }

    @Test
    public void testExecutionOfProjectingMethod() {
        flushTestUsers();

        assertThat(repository.countWithFirstname("Debop")).isEqualTo(1L);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void executesSpecificationCorrectly() {
        flushTestUsers();
        QueryElement<? extends Result<User>> query = (QueryElement<? extends Result<User>>)
            unwrap(repository.getOperations().select(User.class).where(User.FIRSTNAME.eq("Debop")));

        assertThat(repository.findAll(query)).hasSize(1).containsOnly(firstUser);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void executesSingleEntitySpecificationCorrectly() {
        flushTestUsers();
        QueryElement<? extends Result<User>> query = (QueryElement<? extends Result<User>>)
            unwrap(repository.getOperations().select(User.class).where(User.FIRSTNAME.eq("Debop")));

        assertThat(repository.findOne(query)).isPresent();
    }

    @SuppressWarnings("unchecked")
    @Test(expected = IncorrectResultSizeDataAccessException.class)
    public void throwsExceptionForUnderSpecifiedSingleEntitySpecification() {
        flushTestUsers();
        QueryElement<? extends Result<User>> query = (QueryElement<? extends Result<User>>)
            unwrap(repository.getOperations().select(User.class).where(User.FIRSTNAME.like("%e%")));

        // 2개 이상이 나온다
        repository.findOne(query);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void executesCombinedSpecificationsCorrectly() {
        flushTestUsers();

        QueryElement<? extends Result<User>> query = (QueryElement<? extends Result<User>>)
            unwrap(repository.getOperations().select(User.class)
                       .where(User.FIRSTNAME.eq("Debop"))
                       .or(User.LASTNAME.eq("Ahn")));

        assertThat(repository.findAll(query)).hasSize(2).containsOnly(firstUser, secondUser);

    }

    @SuppressWarnings("unchecked")
    @Test
    public void executesNegatingSpecificationCorrectly() {
        flushTestUsers();

        QueryElement<? extends Result<User>> query = (QueryElement<? extends Result<User>>)
            unwrap(repository.getOperations()
                       .select(User.class)
                       .where(User.FIRSTNAME.ne("Debop"))
                       .and(User.LASTNAME.eq("Ahn")));

        assertThat(repository.findAll(query)).hasSize(1).containsOnly(secondUser);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void executesCombinedSpecificationsWithPageableCorrectly() {
        flushTestUsers();

        QueryElement<? extends Result<User>> query = (QueryElement<? extends Result<User>>)
            unwrap(repository.getOperations()
                       .select(User.class)
                       .where(User.FIRSTNAME.eq("Debop"))
                       .or(User.LASTNAME.eq("Ahn")));

        Page<User> users = repository.findAll(query, PageRequest.of(0, 1));
        assertThat(users.getSize()).isEqualTo(1);
        assertThat(users.hasPrevious()).isFalse();
        assertThat(users.getTotalElements()).isEqualTo(2L);
    }

    @Test
    public void executesMethodWithAnnotatedNamedParametersCorrectly() {

        firstUser = repository.save(firstUser);
        secondUser = repository.save(secondUser);

        assertThat(repository.findByLastnameOrFirstname("Ahn", "Debop"))
            .hasSize(2)
            .containsOnly(firstUser, secondUser);
    }

    // BUG: where 절에 반복해서 두 번 나온다.
    @Test
    public void executesMethodWithNamedParametersCorrectlyOnMethodsWithQueryCreation() {

        firstUser = repository.save(firstUser);
        secondUser = repository.save(secondUser);

        assertThat(repository.findByFirstnameOrLastname("Debop", "Ahn"))
            .hasSize(2)
            .containsOnly(firstUser, secondUser);
    }

    @Test
    public void executesLikeAndOrderByCorrectly() {
        flushTestUsers();

        assertThat(repository.findByLastnameLikeOrderByFirstnameDesc("%a%"))
            .hasSize(2)
            .containsExactly(thirdUser, firstUser);
    }

    @Test
    public void executesNotLikeCorrectly() {
        flushTestUsers();

        assertThat(repository.findByLastnameNotLike("%ae%"))
            .hasSize(3)
            .containsOnly(secondUser, thirdUser, fourthUser);
    }

    @Test
    public void executesSimpleNotCorrectly() {
        flushTestUsers();

        assertThat(repository.findByLastnameNot("Bae"))
            .hasSize(3)
            .containsOnly(secondUser, thirdUser, fourthUser);
    }

    @Test
    public void returnsSameListIfNoSpecGiven() {

        flushTestUsers();
        assertSameElements(repository.findAll(), repository.findAll(operations.select(User.class)));
    }

    @Test
    public void returnsSameListIfNoSortIsGiven() {

        flushTestUsers();
        assertSameElements(repository.findAll(Sort.unsorted()), repository.findAll());
    }

    @Test
    public void returnsAllAsPageIfNoPageableIsGiven() {

        flushTestUsers();
        assertThat(repository.findAll(Pageable.unpaged())).isEqualTo(new PageImpl<>(repository.findAll()));
    }

    @Test
    public void removeObject() {

        flushTestUsers();
        long count = repository.count();

        repository.delete(firstUser);
        assertThat(repository.count()).isEqualTo(count - 1);
    }

    @Test
    public void executesPagedSpecificationsCorrectly() {

        Page<User> result = executeSpecWithSort(Sort.unsorted());
        assertThat(result.getContent()).isSubsetOf(firstUser, thirdUser);
    }

    @Test
    public void executesPagedSpecificationsWithSortCorrectly() {

        Page<User> result = executeSpecWithSort(Sort.by(Sort.Direction.ASC, "lastname"));
        assertThat(result.getContent()).contains(firstUser).doesNotContain(secondUser, thirdUser);
    }

    // NOTE: Not Supported
    @Test(expected = AssertionError.class)
    public void executesQueryMethodWithDeepTraversalCorrectly() {

        flushTestUsers();

        firstUser.setManager(secondUser);
        thirdUser.setManager(firstUser);
        repository.saveAll(Arrays.asList(firstUser, thirdUser));

        assertThat(repository.findByManagerLastname("Ahn")).containsOnly(firstUser);
        assertThat(repository.findByManagerLastname("Bae")).containsOnly(thirdUser);
    }

    // NOTE: Not Supported
    @Test(expected = AssertionError.class)
    public void executesFindByColleaguesLastnameCorrectly() {

        flushTestUsers();

        firstUser.getColleagues().add(secondUser);
        thirdUser.getColleagues().add(firstUser);
        repository.saveAll(Arrays.asList(firstUser, thirdUser));

        assertThat(repository.findByColleaguesLastname(secondUser.getLastname())).containsOnly(firstUser);
        assertThat(repository.findByColleaguesLastname("Bae")).containsOnly(secondUser, thirdUser);
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

    @SuppressWarnings("unchecked")
    private Page<User> executeSpecWithSort(Sort sort) {

        flushTestUsers();

        QueryElement<? extends Result<User>> whereClause = (QueryElement<? extends Result<User>>)
            unwrap(operations
                       .select(User.class)
                       .where(User.FIRSTNAME.eq("Debop"))
                       .or(User.LASTNAME.eq("Park")));

        Page<User> result = repository.findAll(whereClause,
                                               PageRequest.of(0, 1, sort));

        assertThat(result.getTotalElements()).isEqualTo(2L);
        return result;
    }

}
