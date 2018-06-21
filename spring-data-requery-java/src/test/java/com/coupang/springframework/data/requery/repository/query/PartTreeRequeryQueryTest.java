package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.domain.AbstractDomainTest;
import com.coupang.springframework.data.requery.domain.sample.User;
import com.coupang.springframework.data.requery.provider.RequeryPersistenceProvider;
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

        requeryQuery.createQueryElement(new Object[] { "Debop", PageRequest.of(0, 1) });
        requeryQuery.createQueryElement(new Object[] { "Debop", PageRequest.of(0, 1) });
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
        User findByFirstname();

        // Wrong property name
        User findByNoSuchPropery(String x);
    }
}
