package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.domain.AbstractDomainTest;
import com.coupang.springframework.data.requery.domain.sample.User;
import com.coupang.springframework.data.requery.provider.RequeryPersistenceProvider;
import io.requery.query.Operator;
import io.requery.query.Result;
import io.requery.query.element.QueryElement;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.DefaultRepositoryMetadata;
import org.springframework.data.repository.query.Param;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings({ "unchecked", "ResultOfMethodCallIgnored" })
@Slf4j
public class PartTreeRequeryQueryTest extends AbstractDomainTest {

    @Rule public ExpectedException thrown = ExpectedException.none();

    RequeryPersistenceProvider provider;

    @Before
    public void setup() {
        this.provider = RequeryPersistenceProvider.of(requeryTemplate);
    }

    @Test
    public void getQueryMethodHasPaging() throws Exception {

        RequeryQueryMethod queryMethod = getQueryMethod("findByFirstname", String.class, Pageable.class);
        PartTreeRequeryQuery requeryQuery = new PartTreeRequeryQuery(queryMethod,
                                                                     requeryTemplate,
                                                                     provider);

        QueryElement<?> query = requeryQuery.createQueryElement(new Object[] { "Debop", PageRequest.of(0, 1) });
        assertThat(query).isNotNull();

        ((QueryElement<? extends Result<?>>) query).get().toList();

        query = requeryQuery.createQueryElement(new Object[] { "Diego", PageRequest.of(1, 3) });
        assertThat(query).isNotNull();

        ((QueryElement<? extends Result<?>>) query).get().toList();
    }

    @Test
    public void cannotIgnoreCaseIfNotString() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Unable to ignore case of java.lang.Integer types, the property 'id' must reference a String");
        testIgnoreCase("findByIdIgnoringCase", 3);
    }

    @Test
    public void cannotIgnoreCaseIfNotStringUnlessIgnoringAll() throws Exception {
        testIgnoreCase("findByIdAllIgnoringCase", 3);
    }

    @Test
    public void recreatesQueryIfNullValueIsGiven() throws Exception {

        RequeryQueryMethod queryMethod = getQueryMethod("findByFirstname", String.class, Pageable.class);
        PartTreeRequeryQuery requeryQuery = new PartTreeRequeryQuery(queryMethod, requeryTemplate, provider);

        // eq 
        QueryElement<?> query = requeryQuery.createQueryElement(new Object[] { "Debop", PageRequest.of(0, 1) });
        assertThat(query).isNotNull();
        Operator operator = query.getWhereElements().iterator().next().getCondition().getOperator();
        assertThat(operator).isEqualTo(Operator.EQUAL);

        ((QueryElement<? extends Result<?>>) query).get().toList();

        // isNull
        query = requeryQuery.createQueryElement(new Object[] { null, PageRequest.of(0, 1) });
        assertThat(query).isNotNull();
        operator = query.getWhereElements().iterator().next().getCondition().getOperator();
        assertThat(operator).isEqualTo(Operator.IS_NULL);

        ((QueryElement<? extends Result<?>>) query).get().toList();
    }

    @Test
    public void shouldLimitExistsProjectionQueries() throws Exception {

        RequeryQueryMethod queryMethod = getQueryMethod("existsByFirstname", String.class);
        PartTreeRequeryQuery requeryQuery = new PartTreeRequeryQuery(queryMethod, requeryTemplate, provider);

        QueryElement<?> query = requeryQuery.createQueryElement(new Object[] { "Debop" });

        assertThat(query.getLimit()).isEqualTo(1);

        ((QueryElement<? extends Result<?>>) query).get().firstOrNull();
    }

    @Test
    public void shouldSelectForExistsProjectionQueries() throws Exception {

        RequeryQueryMethod queryMethod = getQueryMethod("existsByFirstname", String.class);
        PartTreeRequeryQuery requeryQuery = new PartTreeRequeryQuery(queryMethod, requeryTemplate, provider);

        QueryElement<?> query = requeryQuery.createQueryElement(new Object[] { "Debop" });

        ((QueryElement<? extends Result<?>>) query).get().firstOrNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void isEmptyCollection() throws Exception {

        RequeryQueryMethod queryMethod = getQueryMethod("findByRolesIsEmpty");
        PartTreeRequeryQuery requeryQuery = new PartTreeRequeryQuery(queryMethod, requeryTemplate, provider);

        QueryElement<?> query = requeryQuery.createQueryElement(new Object[] {});
    }

    @Test(expected = IllegalArgumentException.class)
    public void isNotEmptyCollection() throws Exception {

        RequeryQueryMethod queryMethod = getQueryMethod("findByRolesIsNotEmpty");
        PartTreeRequeryQuery requeryQuery = new PartTreeRequeryQuery(queryMethod, requeryTemplate, provider);

        QueryElement<?> query = requeryQuery.createQueryElement(new Object[] {});
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsIsEmptyOnNonCollectionProperty() throws Exception {

        RequeryQueryMethod queryMethod = getQueryMethod("findByFirstnameIsEmpty");
        PartTreeRequeryQuery requeryQuery = new PartTreeRequeryQuery(queryMethod, requeryTemplate, provider);

        QueryElement<?> query = requeryQuery.createQueryElement(new Object[] { "Debop" });
    }

    @Test
    public void errorDueToMismatchOfParametersContainNameOfMethodAndInterface() throws Exception {

        RequeryQueryMethod queryMethod = getQueryMethod("findByFirstnameAndLastname", String.class);

        assertThatThrownBy(() -> {
            new PartTreeRequeryQuery(queryMethod, requeryTemplate, provider);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void errorDueToMissingPropertyContainNameOfMethodAndInterface() throws Exception {

        RequeryQueryMethod queryMethod = getQueryMethod("findByNoSuchPropery", String.class);

        assertThatThrownBy(() -> {
            // HINT: PartTree 생성 시, 엔티티에 존재하지 않는 속성명을 사용하는 경우에 예외를 발생시킨다. 
            new PartTreeRequeryQuery(queryMethod, requeryTemplate, provider);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    private void testIgnoreCase(String methodName, Object... values) throws Exception {

        Class<?>[] parameterTypes = new Class[values.length];
        for (int i = 0; i < values.length; i++) {
            parameterTypes[i] = values[i].getClass();
        }

        RequeryQueryMethod queryMethod = getQueryMethod(methodName, parameterTypes);
        PartTreeRequeryQuery requeryQuery = new PartTreeRequeryQuery(queryMethod,
                                                                     requeryTemplate,
                                                                     provider);
        requeryQuery.createQueryElement(values);
    }


    @NotNull
    private RequeryQueryMethod getQueryMethod(String methodName, Class<?>... parameterTypes) throws Exception {
        Method method = UserRepository.class.getMethod(methodName, parameterTypes);

        return new RequeryQueryMethod(method,
                                      new DefaultRepositoryMetadata(UserRepository.class),
                                      new SpelAwareProxyProjectionFactory(),
                                      RequeryPersistenceProvider.of(requeryTemplate));
    }

    @SuppressWarnings("unchecked")
    private static <T> T getValue(Object source, String path) {
        Iterator<String> split = Arrays.asList(path.split("\\.")).iterator();
        Object result = source;

        while (split.hasNext()) {
            Assert.notNull(result, "result must not be null.");
            result = ReflectionTestUtils.getField(result, split.next());
        }

        Assert.notNull(result, "result must not be null.");
        return (T) result;
    }

    @SuppressWarnings({ "SpringDataRepositoryMethodParametersInspection", "SpringDataMethodInconsistencyInspection" })
    interface UserRepository extends Repository<User, Integer> {

        Page<User> findByFirstname(String firstname, Pageable pageable);

        User findByIdIgnoringCase(Integer id);

        User findByIdAllIgnoringCase(Integer id);

        boolean existsByFirstname(String firstname);

        List<User> findByCreatedAtAfter(@Param("refDate") Date refDate);

        List<User> findByRolesIsEmpty();

        List<User> findByRolesIsNotEmpty();

        List<User> findByFirstnameIsEmpty();

        // Wrong number of parameters
        User findByFirstnameAndLastname(String firstname);

        // Wrong property name
        User findByNoSuchPropery(String x);
    }
}
