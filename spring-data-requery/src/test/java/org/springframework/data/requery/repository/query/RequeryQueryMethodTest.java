package org.springframework.data.requery.repository.query;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.DefaultRepositoryMetadata;
import org.springframework.data.repository.query.Param;
import org.springframework.data.requery.annotation.Query;
import org.springframework.data.requery.domain.sample.User;
import org.springframework.data.requery.provider.RequeryPersistenceProvider;
import org.springframework.data.requery.repository.RequeryRepository;
import org.springframework.data.requery.repository.sample.UserRepository;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

/**
 * com.coupang.springframework.data.requery.repository.query.RequeryQueryMethodTest
 *
 * @author debop
 * @since 18. 6. 14
 */
@RunWith(MockitoJUnitRunner.class)
public class RequeryQueryMethodTest {

    static final Class<?> DOMAIN_CLASS = User.class;
    static final String METHOD_NAME = "findByFirstname";

    @Mock RequeryPersistenceProvider extractor;
    @Mock RepositoryMetadata metadata;

    ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    Method invalidReturnType, pageableAndSort, pageableTwice, sortableTwice, findWithLockMethod, findsProjections,
        findsProjection, queryMethodWithCustomEntityFetchGraph;

    @Before
    public void setup() throws Exception {

        invalidReturnType = InvalidRepository.class.getMethod(METHOD_NAME, String.class, Pageable.class);
        pageableAndSort = InvalidRepository.class.getMethod(METHOD_NAME, String.class, Pageable.class, Sort.class);
        pageableTwice = InvalidRepository.class.getMethod(METHOD_NAME, String.class, Pageable.class, Pageable.class);
        sortableTwice = InvalidRepository.class.getMethod(METHOD_NAME, String.class, Sort.class, Sort.class);

//        findWithLockMethod = ValidRepository.class.getQueryMethod("findOneLocked", Integer.class);

        findsProjections = ValidRepository.class.getMethod("findsProjections");
        findsProjection = ValidRepository.class.getMethod("findsProjection");

    }

    private RequeryQueryMethod getQueryMethod(Class<?> repositoryInterface,
                                              String methodName,
                                              Class<?>... parameterTypes) throws Exception {
        Method method = repositoryInterface.getMethod(methodName, parameterTypes);
        DefaultRepositoryMetadata repositoryMetadata = new DefaultRepositoryMetadata(repositoryInterface);

        return new RequeryQueryMethod(method, repositoryMetadata, factory, extractor);
    }

    @Test
    public void testname() throws Exception {

        RequeryQueryMethod method = getQueryMethod(UserRepository.class, "findByLastname", String.class);

        assertThat(method.getName()).isEqualTo("findByLastname");
        assertThat(method.isCollectionQuery()).isTrue();
        assertThat(method.isModifyingQuery()).isFalse();
        assertThat(method.getAnnotatedQuery()).isNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void preventsNullRepositoryMethod() {
        new RequeryQueryMethod(null, metadata, factory, extractor);
    }

    @Test(expected = IllegalArgumentException.class)
    public void preventsNullQueryExtractor() throws Exception {
        Method method = UserRepository.class.getMethod("findByLastname", String.class);
        new RequeryQueryMethod(method, metadata, factory, null);
    }

    @Test
    public void returnsCorrectName() throws Exception {

        RequeryQueryMethod method = getQueryMethod(UserRepository.class, "findByLastname", String.class);
        assertThat(method.getName()).isEqualTo("findByLastname");
    }

    @Test
    public void returnsQueryIfAvailable() throws Exception {

        RequeryQueryMethod method = getQueryMethod(UserRepository.class, "findByLastname", String.class);
        assertThat(method.getAnnotatedQuery()).isNull();

        method = getQueryMethod(UserRepository.class, "findByAnnotatedQuery", String.class);
        assertThat(method.getAnnotatedQuery()).isNotNull();
    }

    @Test(expected = IllegalStateException.class)
    public void rejectsInvalidReturntypeOnPageableFinder() {
        new RequeryQueryMethod(invalidReturnType, metadata, factory, extractor);
    }

    @Test(expected = IllegalStateException.class)
    public void rejectsPageableAndSortInFinderMethod() {
        new RequeryQueryMethod(pageableAndSort, metadata, factory, extractor);
    }

    @Test(expected = IllegalStateException.class)
    public void rejectsTwoPageableParameters() {
        new RequeryQueryMethod(pageableTwice, metadata, factory, extractor);
    }

    @Test(expected = IllegalStateException.class)
    public void rejectsTwoSortableParameters() {
        new RequeryQueryMethod(sortableTwice, metadata, factory, extractor);
    }

    @Test
    public void recognizesModifyingMethod() throws Exception {

        RequeryQueryMethod method = getQueryMethod(UserRepository.class, "renameAllUsersTo", String.class);
        assertThat(method.isModifyingQuery()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsModifyingMethodWithPageable() throws Exception {

        Method method = InvalidRepository.class.getMethod("updateMethod", String.class, Pageable.class);
        new RequeryQueryMethod(method, metadata, factory, extractor);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsModifyingMethodWithSort() throws Exception {
        Method method = InvalidRepository.class.getMethod("updateMethod", String.class, Sort.class);
        new RequeryQueryMethod(method, metadata, factory, extractor);
    }

    @Test
    public void calculatesNamedQueryNamesCorrectly() throws Exception {

        RepositoryMetadata metadata = new DefaultRepositoryMetadata(UserRepository.class);

        RequeryQueryMethod queryMethod = getQueryMethod(UserRepository.class, "findByLastname", String.class);
        assertThat(queryMethod.getNamedQueryName()).isEqualTo("User.findByLastname");

        Method method = UserRepository.class.getMethod("renameAllUsersTo", String.class);
        assertThat(method).isNotNull();
        queryMethod = new RequeryQueryMethod(method, metadata, factory, extractor);
        assertThat(queryMethod.getNamedQueryName()).isEqualTo("User.renameAllUsersTo");

        method = UserRepository.class.getMethod("findSpecialUsersByLastname", String.class);
        assertThat(method).isNotNull();
        queryMethod = new RequeryQueryMethod(method, metadata, factory, extractor);
        assertThat(queryMethod.getNamedQueryName()).isEqualTo("SpecialUser.findSpecialUsersByLastname");
    }

    @Test
    public void rejectsInvalidNamedParameter() throws Exception {

        try {
            getQueryMethod(InvalidRepository.class, "findByAnnotatedQuery", String.class);
            fail("예외가 발생해야 합니다");
        } catch (IllegalStateException e) {
            // Parameter from query
            assertThat(e.getMessage()).contains("foo");
            // Parameter name from annotation
            assertThat(e.getMessage()).contains("param");
            // Method name
            assertThat(e.getMessage()).contains("findByAnnotatedQuery");
        }
    }

    @Test
    public void returnsTrueIfReturnTypeIsEntity() {

        when(metadata.getDomainType()).thenReturn((Class) User.class);
        when(metadata.getReturnedDomainClass(findsProjections)).thenReturn((Class) Integer.class);
        when(metadata.getReturnedDomainClass(findsProjection)).thenReturn((Class) Integer.class);

        assertThat(new RequeryQueryMethod(findsProjections, metadata, factory, extractor).isQueryForEntity()).isFalse();
        assertThat(new RequeryQueryMethod(findsProjection, metadata, factory, extractor).isQueryForEntity()).isFalse();
    }

    static interface InvalidRepository extends Repository<User, Integer> {

        // Invalid return type
        User findByFirstname(String firstname, Pageable pageable);

        // Should not use Pageable *and* Sort
        Page<User> findByFirstname(String firstname, Pageable pageable, Sort sort);

        // Must not use two Pageables
        Page<User> findByFirstname(String firstname, Pageable first, Pageable second);

        // Must not use two Sorts
        Page<User> findByFirstname(String firstname, Sort first, Sort second);

        // Not backed by a named query or @Query annotation
        // @Modifying
        void updateMethod(String firstname);

        // Modifying and Pageable is not allowed
        // @Modifying
        Page<String> updateMethod(String firstname, Pageable pageable);

        // Modifying and Sort is not allowed
        void updateMethod(String firstname, Sort sort);

        // Typo in named parameter
        @Query("select * from SD_User u where u.firstname = :foo")
        List<User> findByAnnotatedQuery(@Param("param") String param);
    }

    static interface ValidRepository extends Repository<User, Integer> {

        @Query(value = "query")
        List<User> findByLastname(String lastname);

        @Query("select * from SD_User u where u.id= ?1")
        List<User> findOne(Integer primaryKey);

        List<Integer> findsProjections();

        Integer findsProjection();

//        @CustomAnnotation
//        void withMetaAnnotation();


        @Query("select * from SD_User u where u.firstname = ?1")
        User queryWithPositionalBinding(@Param("firstname") String firstname);

    }

    static interface RequeryRepositoryOverride extends RequeryRepository<User, Integer> {

        List<User> findAll();

        Optional<User> findOne(Integer id);

        User getOneById(Integer id);
    }
}
