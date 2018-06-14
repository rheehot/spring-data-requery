package com.coupang.springframework.data.requery.repository.support;

import com.coupang.springframework.data.requery.domain.basic.AbstractBasicGroup;
import com.coupang.springframework.data.requery.domain.basic.AbstractBasicUser;
import com.coupang.springframework.data.requery.domain.basic.BasicUser;
import com.coupang.springframework.data.requery.repository.query.DefaultRequeryEntityMetadata;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * DefaultRequeryEntityMetadataTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 12
 */
public class DefaultRequeryEntityMetadataTest {

    @Test(expected = IllegalArgumentException.class)
    public void rejectsNullDomainType() {
        new DefaultRequeryEntityMetadata(null);
    }

    @Test
    public void returnsConfiguredType() {
        DefaultRequeryEntityMetadata<AbstractBasicUser> metadata = new DefaultRequeryEntityMetadata<>(AbstractBasicUser.class);
        assertThat(metadata.getJavaType()).isEqualTo(AbstractBasicUser.class);
    }

    @Test
    public void returnsSimpleClassNameAsEntityNameByDefault() {
        DefaultRequeryEntityMetadata<AbstractBasicGroup> metadata = new DefaultRequeryEntityMetadata<>(AbstractBasicGroup.class);
        assertThat(metadata.getEntityName()).isEqualTo(AbstractBasicGroup.class.getSimpleName());
    }

    @Test
    public void returnsCustomizedEntityNameIfConfigured() {
        DefaultRequeryEntityMetadata<AbstractBasicUser> metadata = new DefaultRequeryEntityMetadata<>(AbstractBasicUser.class);
        assertThat(metadata.getEntityName()).isEqualTo("BasicUser");
        assertThat(metadata.getModelName()).isEqualTo("default");
    }
}
