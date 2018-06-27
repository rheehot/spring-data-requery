package com.coupang.springframework.data.requery.repository.support;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.domain.basic.BasicUser;
import io.requery.meta.EntityModel;
import io.requery.sql.EntityDataStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * SimpleRequeryRepositoryTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 12
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class SimpleRequeryRepositoryTest {

    SimpleRequeryRepository<BasicUser, Long> repo;

    @Mock RequeryOperations operations;
    @Mock EntityDataStore<Object> entityDataStore;
    @Mock EntityModel entityModel;

    @Mock RequeryEntityInformation<BasicUser, Long> information;
    @Mock CrudMethodMetadata metadata;

    @Before
    public void setup() {

        when(operations.getDataStore()).thenReturn(entityDataStore);
        when(operations.getEntityModel()).thenReturn(entityModel);
        when(information.getJavaType()).thenReturn(BasicUser.class);

        repo = new SimpleRequeryRepository<>(information, operations);
        repo.setRepositoryMethodMetadata(metadata);
    }

    @Test
    public void retrieveObjectsForPageableOutOfRange() {
        Optional<BasicUser> user = repo.findById(1L);
        assertThat(user.isPresent()).isFalse();
        // repo.findAll(PageRequest.of(2, 10));
    }

}
