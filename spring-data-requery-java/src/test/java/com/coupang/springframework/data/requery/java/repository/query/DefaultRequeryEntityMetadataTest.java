package com.coupang.springframework.data.requery.java.repository.query;

import com.coupang.springframework.data.requery.java.domain.basic.AbstractBasicUser;
import com.coupang.springframework.data.requery.repository.query.DefaultRequeryEntityMetadata;
import io.requery.Entity;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * com.coupang.springframework.data.requery.java.repository.query.DefaultRequeryEntityMetadataTest
 *
 * @author debop
 * @since 18. 6. 8
 */
public class DefaultRequeryEntityMetadataTest {

    @Test
    public void rejects_null_domainType() {
        Assertions.assertThatThrownBy(() -> {

            new DefaultRequeryEntityMetadata(null);

        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void returnsConfiguredType() {
        DefaultRequeryEntityMetadata<Foo> metadata = new DefaultRequeryEntityMetadata<>(Foo.class);
        assertThat(metadata.getJavaType()).isEqualTo(Foo.class);
    }

    @Test
    public void returnSimpleClassNameAsEntityNameByDefault() {
        DefaultRequeryEntityMetadata<Foo> metadata = new DefaultRequeryEntityMetadata<>(Foo.class);
        assertThat(metadata.getEntityName()).isEqualTo(Foo.class.getSimpleName());
    }

    @Test
    public void returnCustomizedEntityNameIfConfigured() {
        DefaultRequeryEntityMetadata<AbstractBasicUser> metadata = new DefaultRequeryEntityMetadata<>(AbstractBasicUser.class);
        assertThat(metadata.getEntityName()).isEqualTo("BasicUser");
    }


    static class Foo {}

}
