package com.coupang.springframework.data.requery.java.domain.basic;

import com.coupang.springframework.data.requery.java.domain.AbstractDomainTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * com.coupang.springframework.data.requery.java.domain.basic.BasicEntityTest
 *
 * @author debop
 * @since 18. 6. 4
 */
public class BasicEntityTest extends AbstractDomainTest {


    @Test
    public void insertUser() throws InterruptedException {
        BasicUser user = new BasicUser();
        user.setName("test");

        requeryTemplate.upsert(user);

        BasicUser loaded = requeryTemplate.findById(BasicUser.class, user.id);

        assertThat(loaded.getLastModifiedDate()).isNull();
        loaded.name = "updated";
        requeryTemplate.update(loaded);

        assertThat(loaded.getLastModifiedDate()).isNotNull();

        Thread.sleep(100L);
    }
}
