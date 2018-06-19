package com.coupang.springframework.data.requery.repository.support;

import com.coupang.springframework.data.requery.domain.AbstractDomainTest;
import com.coupang.springframework.data.requery.domain.RandomData;
import com.coupang.springframework.data.requery.domain.basic.BasicUser;
import com.coupang.springframework.data.requery.utils.EntityUtils;
import com.coupang.springframework.data.requery.utils.PagingUtils;
import io.requery.query.*;
import io.requery.query.element.QueryElement;
import io.requery.query.function.Count;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * SimpleRequeryRepositoryQueryTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 19
 */
@Slf4j
public class SimpleRequeryRepositoryQueryTest extends AbstractDomainTest {

    private final int USER_COUNT = 10;
    Set<BasicUser> users;

    @Before
    public void setup() {
        requeryTemplate.deleteAll(BasicUser.class);

        users = RandomData.randomUsers(USER_COUNT);
        requeryTemplate.insertAll(users);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findAllByIds() {
        Set<Long> ids = users.stream().map(BasicUser::getId).collect(Collectors.toSet());

        NamedExpression<Long> keyExpr = (NamedExpression<Long>) EntityUtils.getKeyExpression(BasicUser.class);

        List<BasicUser> savedUsers = requeryTemplate
            .select(BasicUser.class)
            .where(keyExpr.in(ids))
            .get()
            .toList();

        assertThat(savedUsers).hasSize(USER_COUNT);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void deleteAllByIds() {
        Set<Long> ids = users.stream().map(BasicUser::getId).collect(Collectors.toSet());

        NamedExpression<Long> keyExpr = (NamedExpression<Long>) EntityUtils.getKeyExpression(BasicUser.class);

        Integer deletedCount = requeryTemplate
            .delete(BasicUser.class)
            .where(keyExpr.in(ids))
            .get()
            .value();

        assertThat(deletedCount).isEqualTo(USER_COUNT);
    }

    @Test
    public void findAllWithSort() {
        Sort sort = Sort.by(Sort.Order.desc("birthday"), Sort.Order.asc("name"));

        OrderingExpression<?>[] orderingExprs = PagingUtils.toRequeryOrderExpression(BasicUser.class, sort);

        List<BasicUser> orderedUsers = requeryTemplate
            .select(BasicUser.class)
            .orderBy(orderingExprs)
            .get()
            .toList();

        assertThat(orderedUsers).hasSize(USER_COUNT);
        orderedUsers.forEach(user -> log.debug("user={}", user));

        for (int i = 0; i < USER_COUNT - 1; i++) {
            assertThat(orderedUsers.get(i).getBirthday().compareTo(orderedUsers.get(i + 1).getBirthday())).isGreaterThanOrEqualTo(0);
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findAllWithPageableSlice() {
        Pageable pageable = PageRequest.of(1, 3);

        Return<Result<BasicUser>> query = PagingUtils.applyPageable(BasicUser.class,
                                                                    (QueryElement<Result<BasicUser>>) requeryTemplate.select(BasicUser.class),
                                                                    pageable);

        List<BasicUser> content = query.get().toList();

        long total = requeryTemplate.count(BasicUser.class).get().value().longValue();

        Page<BasicUser> userPage = new PageImpl<>(content, pageable, total);


        assertThat(userPage.getTotalElements()).isEqualTo(USER_COUNT);
        assertThat(userPage.getContent()).hasSize(3);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void existsById() {

        Long userId = users.iterator().next().getId();
        NamedExpression<Long> keyExpr = (NamedExpression<Long>) EntityUtils.getKeyExpression(BasicUser.class);

        Tuple result = requeryTemplate
            .select(Count.count(BasicUser.class).as("count"))
            .where(keyExpr.eq(userId))
            .get()
            .first();

        assertThat(result.count()).isEqualTo(1);
        assertThat(result.<Integer>get("count")).isEqualTo(1);
    }
}
