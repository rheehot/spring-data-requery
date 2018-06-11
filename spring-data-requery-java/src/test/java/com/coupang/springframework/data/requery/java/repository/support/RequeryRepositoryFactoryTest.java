package com.coupang.springframework.data.requery.java.repository.support;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.java.domain.basic.AbstractBasicUser;
import com.coupang.springframework.data.requery.java.repository.custom.CustomGenericRequeryRepositoryFactory;
import com.coupang.springframework.data.requery.java.repository.custom.UserCustomExtendedRepository;
import com.coupang.springframework.data.requery.repository.RequeryRepository;
import com.coupang.springframework.data.requery.repository.support.RequeryEntityInformation;
import com.coupang.springframework.data.requery.repository.support.RequeryRepositoryFactory;
import com.coupang.springframework.data.requery.repository.support.SimpleRequeryRepository;
import io.requery.meta.EntityModel;
import io.requery.sql.EntityDataStore;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


/**
 * com.coupang.springframework.data.requery.java.repository.support.RequeryRepositoryFactoryTest
 *
 * @author debop
 * @since 18. 6. 9
 */
@Slf4j
@RunWith(MockitoJUnitRunner.Silent.class)
public class RequeryRepositoryFactoryTest {

    RequeryRepositoryFactory factory;

    @Mock RequeryOperations requeryOperations;
    @Mock EntityDataStore entityDataStore;
    @Mock EntityModel entityModel;
    @Mock @SuppressWarnings("rawtypes") RequeryEntityInformation entityInformation;

    @Before
    public void setup() {
        when(requeryOperations.getDataStore()).thenReturn(entityDataStore);

        factory = new RequeryRepositoryFactory(requeryOperations) {
            @Override
            public <T, ID> RequeryEntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
                return entityInformation;
            }
        };
        factory.setQueryLookupStrategyKey(QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND);
    }

    @Test
    public void setupBasicInstanceCorrectly() throws Exception {
        assertThat(factory.getRepository(SimpleSampleRepository.class)).isNotNull();
    }

    @Test
    public void allowsCallingOfObjectMethods() {

        SimpleSampleRepository repository = factory.getRepository(SimpleSampleRepository.class);

        repository.hashCode();
        repository.toString();
        repository.equals(repository);
    }

    @Test
    public void capturesMissingCustomImplementationAndProvidesInterfacename() throws Exception {
        try {
            factory.getRepository(SampleRepository.class);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).contains(SampleRepository.class.getName());
        } catch (NotImplementedException ne) {
            log.error("구현 중", ne);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void handlesRuntimeExceptionsCorrectly() {

        SampleRepository repository = factory.getRepository(SampleRepository.class, new SampleCustomRepositoryImpl());
        repository.throwingRuntimeException();
    }


    @Test(expected = IOException.class)
    public void handlesCheckedExceptionsCorrectly() throws Exception {

        SampleRepository repository = factory.getRepository(SampleRepository.class, new SampleCustomRepositoryImpl());
        repository.throwingCheckedException();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void createsProxyWithCustomBaseClass() {

        RequeryRepositoryFactory factory = new CustomGenericRequeryRepositoryFactory(requeryOperations);
        factory.setQueryLookupStrategyKey(QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND);
        UserCustomExtendedRepository repository = factory.getRepository(UserCustomExtendedRepository.class);

        repository.customMethod(1L);
    }


    private interface SimpleSampleRepository extends RequeryRepository<AbstractBasicUser, Long> {

        @Transactional
        Optional<AbstractBasicUser> findById(Long id);
    }

    public interface SampleCustomRepository {

        void throwingRuntimeException();

        void throwingCheckedException() throws IOException;
    }

    private class SampleCustomRepositoryImpl implements SampleCustomRepository {

        @Override
        public void throwingRuntimeException() {
            throw new IllegalArgumentException("You lose!");
        }

        @Override
        public void throwingCheckedException() throws IOException {
            throw new IOException("You lose!");
        }
    }

    private interface SampleRepository extends RequeryRepository<AbstractBasicUser, Long>, SampleCustomRepository {

    }

    static class CustomRequeryRepository<T, ID> extends SimpleRequeryRepository<T, ID> {

        public CustomRequeryRepository(@NotNull RequeryEntityInformation<T, ID> entityInformation,
                                       @NotNull RequeryOperations operations) {
            super(entityInformation, operations);
        }
    }

}
