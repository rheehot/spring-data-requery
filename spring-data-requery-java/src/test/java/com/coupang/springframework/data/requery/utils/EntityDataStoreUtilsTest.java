package com.coupang.springframework.data.requery.utils;

import com.coupang.springframework.data.requery.core.EntityState;
import com.coupang.springframework.data.requery.domain.AbstractDomainTest;
import com.coupang.springframework.data.requery.domain.basic.BasicUser;
import io.requery.meta.EntityModel;
import io.requery.meta.Type;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * EntityDataStoreUtilsTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 7
 */
@Slf4j
public class EntityDataStoreUtilsTest extends AbstractDomainTest {

    @Test
    public void retrieveEntityModel() {
        EntityModel entityModel = RequeryUtils.getEntityModel(requeryTemplate.getDataStore());

        assertThat(entityModel).isNotNull();

        assertThat(entityModel.getName()).isEqualTo("default");
        assertThat(entityModel.containsTypeOf(BasicUser.class)).isTrue();

        for (Type type : entityModel.getTypes()) {
            log.debug("Entity class={}", type.getClassType());
        }
    }

    @Test
    public void retrieveEntityClasses() {
        List<Class<?>> classes = RequeryUtils.getEntityClasses(requeryTemplate.getDataStore());

        assertThat(classes.contains(BasicUser.class)).isTrue();
        assertThat(classes.contains(EntityState.class)).isFalse();
    }
}
