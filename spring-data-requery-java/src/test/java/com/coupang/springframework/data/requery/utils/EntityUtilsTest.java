package com.coupang.springframework.data.requery.utils;

import com.coupang.springframework.data.requery.domain.basic.BasicGroup;
import com.coupang.springframework.data.requery.domain.basic.BasicUser;
import io.requery.query.NamedExpression;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * com.coupang.springframework.data.requery.utils.EntityUtilsTest
 *
 * @author debop
 * @since 18. 6. 18
 */
public class EntityUtilsTest {

    @Test
    public void retrieveKeyPropertyFromBasicUser() {
        NamedExpression<?> keyExpr = EntityUtils.getKeyExpression(BasicUser.class);

        assertThat(keyExpr).isNotNull();
        assertThat(keyExpr.getName()).isEqualTo("id");
        assertThat(keyExpr.getClassType()).isEqualTo(Long.class);
    }

    @Test
    public void retrieveKeyPropertyFromBasicGroup() {
        NamedExpression<?> keyExpr = EntityUtils.getKeyExpression(BasicGroup.class);

        assertThat(keyExpr).isNotNull();
        assertThat(keyExpr.getName()).isEqualTo("id");
        assertThat(keyExpr.getClassType()).isEqualTo(Integer.class);
    }
}
