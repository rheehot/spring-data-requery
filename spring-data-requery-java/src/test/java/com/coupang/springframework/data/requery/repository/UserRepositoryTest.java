package com.coupang.springframework.data.requery.repository;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.domain.sample.AbstractUser;
import com.coupang.springframework.data.requery.domain.sample.Role;
import com.coupang.springframework.data.requery.domain.sample.SpecialUser;
import com.coupang.springframework.data.requery.domain.sample.User;
import com.coupang.springframework.data.requery.repository.config.EnableRequeryRepositories;
import com.coupang.springframework.data.requery.repository.config.InfrastructureConfig;
import com.coupang.springframework.data.requery.repository.sample.RoleRepository;
import com.coupang.springframework.data.requery.repository.sample.UserRepository;
import com.coupang.springframework.data.requery.repository.sample.UserRepositoryImpl;
import com.coupang.springframework.data.requery.repository.support.RequeryRepositoryFactoryBean;
import com.coupang.springframework.data.requery.repository.support.SimpleRequeryRepository;
import io.requery.query.Result;
import io.requery.query.Tuple;
import io.requery.query.element.QueryElement;
import io.requery.sql.StatementExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Ignore;
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

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static com.coupang.springframework.data.requery.utils.RequeryUtils.unwrap;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.ExampleMatcher.matching;

/**
 * UserRepositoryTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 28
 */
@Slf4j
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
        user.setActive(true);
        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        user.getRoles().addAll(Arrays.asList(roles));

        return user;
    }


    @Before
    public void setup() throws Exception {

        firstUser = createUser("Debop", "Bae", "debop@coupang.com");
        firstUser.setAge(51);

        secondUser = createUser("Diego", "Ahn", "diego@coupang.com");
        secondUser.setAge(30);

        Thread.sleep(10);

        thirdUser = createUser("Jinie", "Park", "jinie@coupang.com");
        thirdUser.setAge(26);

        fourthUser = createUser("Nickoon", "Jeon", "nickoon@coupang.com");
        fourthUser.setAge(35);

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

        User foundPerson = repository.findById(id).orElseThrow(() -> new IllegalStateException("Not Found"));
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

        repository.deleteById(requireNonNull(firstUser.getId()));
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

        assertThat(repository.existsById(requireNonNull(firstUser.getId()))).isFalse();
        assertThat(repository.existsById(requireNonNull(secondUser.getId()))).isFalse();

        assertThat(repository.count()).isEqualTo(before - 2);
    }

    @Test
    public void batchDeleteCollectionOfEntities() {
        flushTestUsers();

        long before = repository.count();

        repository.deleteInBatch(Arrays.asList(firstUser, secondUser));

        assertThat(repository.existsById(requireNonNull(firstUser.getId()))).isFalse();
        assertThat(repository.existsById(requireNonNull(secondUser.getId()))).isFalse();

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

        User firstReferenceUser = repository.findById(requireNonNull(firstUser.getId())).get();
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

        User reference = repository.findById(requireNonNull(firstUser.getId())).get();
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

    @Test
    public void executesFindByNotNullLastnameCorrectly() {

        flushTestUsers();

        assertThat(repository.findByLastnameNotNull()).containsOnly(firstUser, secondUser, thirdUser, fourthUser);
    }

    @Test
    public void findsSortedByLastname() {

        flushTestUsers();

        assertThat(repository.findByEmailAddressLike("%@%", Sort.by(Sort.Direction.ASC, "lastname")))
            .containsExactly(secondUser, firstUser, fourthUser, thirdUser);
    }

    @Test
    public void readsPageWithGroupByClauseCorrectly() {

        flushTestUsers();

        Page<String> result = repository.findByLastnameGrouped(PageRequest.of(0, 10));
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @Test
    public void executesLessThatOrEqualQueriesCorrectly() {

        flushTestUsers();

        assertThat(repository.findByAgeLessThanEqual(35)).containsOnly(secondUser, thirdUser, fourthUser);
    }

    @Test
    public void executesGreaterThatOrEqualQueriesCorrectly() {

        flushTestUsers();

        assertThat(repository.findByAgeGreaterThanEqual(35)).containsOnly(firstUser, fourthUser);
    }

    @Test
    public void executesNativeQueryCorrectly() {

        flushTestUsers();
        assertThat(repository.findNativeByLastname("Bae")).containsOnly(firstUser);
    }

    @Test
    public void executesFinderWithTrueKeywordCorrectly() {

        flushTestUsers();
        firstUser.setActive(false);
        repository.upsert(firstUser);

        assertThat(repository.findByActiveTrue()).containsOnly(secondUser, thirdUser, fourthUser);
    }

    @Test
    public void executesFinderWithFalseKeywordCorrectly() {

        flushTestUsers();
        firstUser.setActive(false);
        repository.upsert(firstUser);

        assertThat(repository.findByActiveFalse()).containsOnly(firstUser);
    }

    @Test
    public void executesAnnotatedCollectionMethodCorrectly() throws InterruptedException {

        flushTestUsers();

        firstUser.getColleagues().add(thirdUser);
        repository.save(firstUser);

        List<User> result = repository.findColleaguesFor(firstUser.getId());
        assertThat(result).containsOnly(thirdUser);
    }

    @Test
    public void executesFinderWithAfterKeywordCorrectly() {

        flushTestUsers();
        assertThat(repository.findByCreatedAtAfter(secondUser.getCreatedAt())).containsOnly(thirdUser, fourthUser);
    }

    @Test
    public void executesFinderWithBeforeKeywordCorrectly() {

        flushTestUsers();
        assertThat(repository.findByCreatedAtBefore(thirdUser.getCreatedAt())).containsOnly(firstUser, secondUser);
    }

    @Test
    public void executesFinderWithStartingWithCorrectly() {

        flushTestUsers();
        assertThat(repository.findByFirstnameStartingWith("Deb")).containsOnly(firstUser);
    }

    @Test
    public void executesFinderWithEndingWithCorrectly() {

        flushTestUsers();
        assertThat(repository.findByFirstnameEndingWith("bop")).containsOnly(firstUser);
    }

    @Test
    public void executesFinderWithContainingCorrectly() {

        flushTestUsers();
        assertThat(repository.findByFirstnameContaining("n")).containsOnly(thirdUser, fourthUser);
    }

    @Test
    public void allowsExecutingPageableMethodWithUnpagedArgument() {

        flushTestUsers();

        assertThat(repository.findByFirstname("Debop", null)).containsOnly(firstUser);

        Page<User> page = repository.findByFirstnameIn(Pageable.unpaged(), "Debop", "Diego");
        assertThat(page).isNotNull();
        assertThat(page.getNumberOfElements()).isEqualTo(2);
        assertThat(page.getContent()).containsOnly(firstUser, secondUser);

        page = repository.findAll(Pageable.unpaged());
        assertThat(page.getNumberOfElements()).isEqualTo(4);
        assertThat(page.getContent()).contains(firstUser, secondUser, thirdUser, fourthUser);
    }

    @Test
    public void executesNativeQueryForNonEntitiesCorrectly() {

        flushTestUsers();

        List<Tuple> result = repository.findOnesByNativeQuery();

        assertThat(result.size()).isEqualTo(4);
        assertThat(result.get(0).<Integer>get(0)).isEqualTo(1);
    }

    @Test
    public void handlesIterableOfIdsCorrectly() {

        flushTestUsers();

        Set<Integer> set = new HashSet<>();
        set.add(firstUser.getId());
        set.add(secondUser.getId());

        assertThat(repository.findAllById(set)).containsOnly(firstUser, secondUser);
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

    @Test
    public void ordersByReferencedEntityCorrectly() {

        flushTestUsers();
        firstUser.setManager(thirdUser);
        repository.upsert(firstUser);

        Page<User> all = repository.findAll(PageRequest.of(0, 10, Sort.by("manager")));

        assertThat(all.getContent()).isNotEmpty();
    }

    @Test
    public void bindsSortingToOuterJoinCorrectly() {

        flushTestUsers();

        Page<User> result = repository.findAllPaged(PageRequest.of(0, 10, Sort.by("manager.lastname")));
        assertThat(result.getContent()).hasSize((int) repository.count());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void doesNotDropNullValuesOnPagedSpecificationExecution() {

        flushTestUsers();

        // NOTE: Not support association property. (ex, manager.lastname)
        QueryElement<?> whereClause = unwrap(operations.select(User.class).where(User.LASTNAME.eq("Bae")));
        Page<User> page = repository.findAll((QueryElement<? extends Result<User>>) whereClause,
                                             PageRequest.of(0, 20, Sort.by("manager.lastname")));

        assertThat(page.getNumberOfElements()).isEqualTo(1);
        assertThat(page).containsOnly(firstUser);
    }

    @Test
    public void shouldGenerateLeftOuterJoinInfindAllWithPaginationAndSortOnNestedPropertyPath() {

        firstUser.setManager(null);
        secondUser.setManager(null);
        thirdUser.setManager(firstUser);    // manager Debop
        fourthUser.setManager(secondUser);  // manager Diego

        flushTestUsers();

        // NOTE: Not support association property. (ex, manager.lastname)
        Page<User> pages = repository.findAll(PageRequest.of(0, 4, Sort.by("manager")));

        assertThat(pages.getSize()).isEqualTo(4);
        assertThat(pages.getContent().get(0).getManager()).isNull();
        assertThat(pages.getContent().get(1).getManager()).isNull();
        assertThat(pages.getContent().get(2).getManager().getFirstname()).isEqualTo("Debop");
        assertThat(pages.getContent().get(3).getManager().getFirstname()).isEqualTo("Diego");
    }

    @Test
    public void executesManualQueryWithPositionLikeExpressionCorrectly() {

        flushTestUsers();

        List<User> result = repository.findByFirstnameLike("Ni%");
        assertThat(result).containsOnly(fourthUser);
    }

    // NOTE: Not supported Named parameter
    @Test(expected = StatementExecutionException.class)
    public void executesManualQueryWithNamedLikeExpressionCorrectly() {

        flushTestUsers();

        List<User> result = repository.findByFirstnameLikeNamed("Ni%");
        assertThat(result).containsOnly(fourthUser);
    }

    @Test
    public void executesDerivedCountQueryToLong() {

        flushTestUsers();
        assertThat(repository.countByLastname("Bae")).isEqualTo(1L);
    }

    @Test
    public void executesDerivedCountQueryToInt() {

        flushTestUsers();

        assertThat(repository.countUsersByFirstname("Debop")).isEqualTo(1);
    }

    @Test
    public void executesDerivedExistsQuery() {

        flushTestUsers();

        assertThat(repository.existsByLastname("Bae")).isTrue();
        assertThat(repository.existsByLastname("Donald Trump")).isFalse();
    }

    @Test
    public void findAllReturnsEmptyIterableIfNoIdsGiven() {

        assertThat(repository.findAllById(Collections.emptySet())).isEmpty();
    }

    @Ignore("결과가 Tuple 인 경우 ReturnedType 으로 변환하는 기능이 필요하다")
    @Test
    public void executesManuallyDefinedQueryWithFieldProjection() {

        flushTestUsers();
        List<String> firstnames = repository.findFirstnamesByLastname("Bae");

        firstnames.forEach(firstname -> log.debug("firstname={}", firstname));
        assertThat(firstnames).containsOnly("Debop");
    }

    @Test
    public void looksUpEntityReference() {

        flushTestUsers();

        User result = repository.getOne(firstUser.getId());
        assertThat(result).isEqualTo(firstUser);
    }

    @Test
    public void invokesQueryWithVarargsParametersCorrectly() {

        flushTestUsers();

        Collection<User> result = repository.findByIdIn(firstUser.getId(), secondUser.getId());
        assertThat(result).containsOnly(firstUser, secondUser);
    }

    @Test
    public void shouldSupportModifyingQueryWithVarArgs() {

        flushTestUsers();

        repository.updateUserActiveState(false, firstUser.getId(), secondUser.getId(), thirdUser.getId(), fourthUser.getId());

        long expectedCount = repository.count();
        assertThat(repository.findByActiveFalse()).hasSize((int) expectedCount);
        assertThat(repository.findByActiveTrue()).isEmpty();
    }

    @Test
    public void executesFinderWithOrderClauseOnly() {

        flushTestUsers();

        assertThat(repository.findAllByOrderByLastnameAsc())
            .containsExactly(secondUser, firstUser, fourthUser, thirdUser);
    }


    // NOTE: Not Supported
    @Test
    public void sortByEmbeddedProperty() {

        thirdUser.getAddress().setCountry("South Korea");
        thirdUser.getAddress().setCity("Seoul");
        thirdUser.getAddress().setStreetName("Songpa");
        thirdUser.getAddress().setStreetNo("570");

        flushTestUsers();

        Page<User> page = repository.findAll(PageRequest.of(0, 10, Sort.by("address.streetName")));
        assertThat(page.getContent()).hasSize(4);

        // Not supported
        // assertThat(page.getContent().get(3)).isEqualTo(thirdUser);
    }

    @Test
    public void findsUserByBinaryDataReference() {

        flushTestUsers();

        Collection<User> result = repository.findByIdsCustomWithPositionalVarArgs(firstUser.getId(), secondUser.getId());

        assertThat(result).containsOnly(firstUser, secondUser);
    }

    @Test
    public void customFindByQueryWithNamedVarargsParameters() {

        flushTestUsers();

        Collection<User> result = repository.findByIdsCustomWithNamedVarArgs(firstUser.getId(), secondUser.getId());

        assertThat(result).containsOnly(firstUser, secondUser);
    }

    @Ignore("Not support derived class from entity class")
    @Test
    public void saveAndFlushShouldSupportReturningSubTypesOfRepositoryEntity() {

        SpecialUser user = new SpecialUser();
        user.setFirstname("Thomas");
        user.setEmailAddress("thomas@example.org");

        // HINT: Entity class 를 상속받은 Derived class를 부모 클래스용 Repository를 사용하면 안된다.
        SpecialUser savedUser = repository.insert(user);

        assertThat(savedUser.getFirstname()).isEqualTo(user.getFirstname());
        assertThat(savedUser.getEmailAddress()).isEqualTo(user.getEmailAddress());
    }

    @Test
    public void findAllByUntypedExampleShouldReturnSubTypesOfRepositoryEntity() {

        flushTestUsers();

        List<User> result = repository.findAll(Example.of(createUser(null, null, null),
                                                          matching().withIgnorePaths("age", "createdAt", "dateOfBirth")));
        assertThat(result).hasSize(4);
    }

    @Test
    public void findAllByTypedUserExampleShouldReturnSubTypesOfRepositoryEntity() {

        flushTestUsers();

        Example<User> example = Example.of(createUser(null, null, null),
                                           matching().withIgnorePaths("age", "createdAt", "dateOfBirth"));
        List<User> result = repository.findAll(example);

        assertThat(result).hasSize(4);
    }

    @Test
    public void deleteByShouldReturnListOfDeletedElementsWhenRetunTypeIsCollectionLike() {

        flushTestUsers();

        Integer deletedCount = repository.deleteByLastname(firstUser.getLastname());
        assertThat(deletedCount).isEqualTo(1);

        assertThat(repository.countByLastname(firstUser.getLastname())).isEqualTo(0L);
    }

    @Test
    public void deleteByShouldReturnNumberOfEntitiesRemovedIfReturnTypeIsInteger() {

        flushTestUsers();

        Integer removedCount = repository.removeByLastname(firstUser.getLastname());
        assertThat(removedCount).isEqualTo(1);
    }


    @Test
    public void deleteByShouldReturnZeroInCaseNoEntityHasBeenRemovedAndReturnTypeIsNumber() {

        flushTestUsers();

        Integer removedCount = repository.removeByLastname("Not exists");
        assertThat(removedCount).isEqualTo(0);
    }

    @Ignore("Tuple 을 returned type 으로 추출하는 작업이 필요하다.")
    @Test
    public void findBinaryDataByIdNative() {

        byte[] data = "Woho!!".getBytes(StandardCharsets.UTF_8);
        firstUser.setBinaryData(data);

        flushTestUsers();

        // TODO: Tuple 을 returned type 으로 추출하는 작업이 필요하다.
        // TODO: @Convert 를 사용한 property 에 대해서 변환작업도 필요하다. Blob 를 byte[] 로 바꾸는 ...
        byte[] result = repository.findBinaryDataByIdNative(firstUser.getId());

        assertThat(result).isNotNull();
        assertThat(result.length).isEqualTo(data.length);
        assertThat(result).isEqualTo(data);
    }

    @Test
    public void findPaginatedExplicitQueryWithEmpty() {

        firstUser.setFirstname(null);

        flushTestUsers();

        Page<User> result = repository.findAllByFirstnameLike("%", PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(3);
    }

    @Test
    public void findPaginatedExplicitQuery() {

        flushTestUsers();

        Page<User> result = repository.findAllByFirstnameLike("De%", PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    public void findOldestUser() {

        flushTestUsers();

        User oldest = firstUser;

        assertThat(repository.findFirstByOrderByAgeDesc()).isEqualTo(oldest);
        assertThat(repository.findFirst1ByOrderByAgeDesc()).isEqualTo(oldest);
    }

    @Test
    public void findYoungestUser() {

        flushTestUsers();

        User youngest = thirdUser;

        assertThat(repository.findTopByOrderByAgeAsc()).isEqualTo(youngest);
        assertThat(repository.findTop1ByOrderByAgeAsc()).isEqualTo(youngest);
    }

    @Test
    public void find2OldestUser() {

        flushTestUsers();

        User oldest1 = firstUser;
        User oldest2 = fourthUser;

        assertThat(repository.findFirst2ByOrderByAgeDesc()).containsOnly(oldest1, oldest2);
        assertThat(repository.findTop2ByOrderByAgeDesc()).containsOnly(oldest1, oldest2);
    }

    @Test
    public void find2YoungestUser() {

        flushTestUsers();

        User youngest1 = thirdUser;
        User youngest2 = secondUser;

        assertThat(repository.findFirst2UsersBy(Sort.by("age"))).containsOnly(youngest1, youngest2);
        assertThat(repository.findTop2UsersBy(Sort.by("age"))).containsOnly(youngest1, youngest2);
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
