package com.coupang.springframework.data.requery.repository.support;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.domain.basic.AbstractBasicUser;
import io.requery.meta.Attribute;
import io.requery.meta.EntityModel;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * RequeryEntityInformationSupportTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 12
 */
@RunWith(MockitoJUnitRunner.class)
public class RequeryEntityInformationSupportTest {

    @Mock RequeryOperations operations;
    @Mock EntityModel entityModel;

    @Test
    public void usesSimpleClassNameIfNoEntityNameGiven() throws Exception {

        RequeryEntityInformation<User, Long> information = new DummyRequeryEntityInformation<>(User.class);
        assertThat(information.getEntityName()).isEqualTo("User");
        assertThat(information.getModelName()).isNullOrEmpty();

        RequeryEntityInformation<AbstractBasicUser, Long> second = new DummyRequeryEntityInformation<>(AbstractBasicUser.class);
        assertThat(second.getEntityName()).isEqualTo("BasicUser");
        assertThat(second.getModelName()).isEqualTo("default");
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsClassNotBeingFoundInMetamodel() {

        Mockito.when(operations.getEntityModel()).thenReturn(entityModel);
        RequeryEntityInformationSupport.getEntityInformation(User.class, operations);
    }

    static class User {

    }

    static class DummyRequeryEntityInformation<T, ID> extends RequeryEntityInformationSupport<T, ID> {

        public DummyRequeryEntityInformation(Class<T> domainClass) {
            super(domainClass);
        }

        @Override
        public @Nullable Attribute<? super T, ?> getIdAttribute() {
            return null;
        }

        @Override
        public boolean hasCompositeId() {
            return false;
        }

        @Override
        public Iterable<String> getIdAttributeNames() {
            return Collections.emptySet();
        }

        @Override
        public @Nullable Object getCompositeIdAttributeValue(Object id, String idAttribute) {
            return null;
        }

        @Override
        public ID getId(T entity) {
            return null;
        }

        @Override
        public Class<ID> getIdType() {
            return null;
        }
    }
}
