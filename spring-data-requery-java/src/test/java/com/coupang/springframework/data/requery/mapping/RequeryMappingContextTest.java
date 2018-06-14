package com.coupang.springframework.data.requery.mapping;

import com.coupang.springframework.data.requery.mapping.RequeryMappingContext;
import com.coupang.springframework.data.requery.mapping.RequeryPersistentEntity;
import io.requery.Key;
import io.requery.Version;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * com.coupang.springframework.data.requery.mapping.RequeryMappingContextTest
 *
 * @author debop
 * @since 18. 6. 8
 */
public class RequeryMappingContextTest {

    @Test
    public void requeryPersistentEntityRejectsSpringDataAtVersionAnnotation() {

        RequeryMappingContext context = new RequeryMappingContext();
        RequeryPersistentEntity entity = context.getRequiredPersistentEntity(Sample.class);

        assertThat(entity).isNotNull();

        assertThat(entity.getRequiredPersistentProperty("id").isIdProperty()).isTrue();
        assertThat(entity.getRequiredPersistentProperty("springId").isIdProperty()).isFalse();

        assertThat(entity.getRequiredPersistentProperty("version").isVersionProperty()).isTrue();
        assertThat(entity.getRequiredPersistentProperty("springVersion").isVersionProperty()).isFalse();
    }


    static class Sample {

        @Key
        long id;

        @org.springframework.data.annotation.Id
        long springId;

        @Version
        long version;

        @org.springframework.data.annotation.Version
        long springVersion;
    }
}
