package com.coupang.springframework.data.requery.java.repository.support;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.java.domain.basic.AbstractBasicUser;
import com.coupang.springframework.data.requery.repository.RequeryRepository;
import com.coupang.springframework.data.requery.repository.support.RequeryEntityInformation;
import com.coupang.springframework.data.requery.repository.support.RequeryRepositoryFactory;
import io.requery.meta.EntityModel;
import io.requery.sql.EntityDataStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


/**
 * com.coupang.springframework.data.requery.java.repository.support.RequeryRepositoryFactoryTest
 *
 * @author debop
 * @since 18. 6. 9
 */
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


    private interface SimpleSampleRepository extends RequeryRepository<AbstractBasicUser, Long> {

        @Transactional
        Optional<AbstractBasicUser> findById(Long id);
    }
}
