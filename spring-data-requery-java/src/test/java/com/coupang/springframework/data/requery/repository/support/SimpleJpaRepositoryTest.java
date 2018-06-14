package com.coupang.springframework.data.requery.repository.support;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.domain.basic.BasicUser;
import com.coupang.springframework.data.requery.repository.support.CrudMethodMetadata;
import com.coupang.springframework.data.requery.repository.support.RequeryEntityInformation;
import com.coupang.springframework.data.requery.repository.support.SimpleRequeryRepository;
import io.requery.meta.EntityModel;
import io.requery.sql.EntityDataStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;

import static org.mockito.Mockito.when;

/**
 * SimpleJpaRepositoryTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 12
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class SimpleJpaRepositoryTest {

    SimpleRequeryRepository<BasicUser, Long> repo;

    @Mock RequeryOperations operations;
    @Mock EntityDataStore entityDataStore;
    @Mock EntityModel entityModel;

    @Mock RequeryEntityInformation<BasicUser, Long> information;
    @Mock CrudMethodMetadata metadata;

    @Before
    public void setup() {

        when(operations.getDataStore()).thenReturn(entityDataStore);
        when(operations.getEntityModel()).thenReturn(entityModel);

        repo = new SimpleRequeryRepository<>(information, operations);
        repo.setRepositoryMethodMetadata(metadata);
    }

    @Test
    public void retrieveObjectsForPageableOutOfRange() {

        repo.findAll(PageRequest.of(2, 10));
    }
}
