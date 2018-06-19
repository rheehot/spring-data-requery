package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.domain.AbstractDomainTest;
import com.coupang.springframework.data.requery.domain.RandomData;
import com.coupang.springframework.data.requery.domain.basic.BasicUser;
import io.requery.query.Result;
import io.requery.query.Return;
import io.requery.query.WhereAndOr;
import io.requery.query.element.QueryElement;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.ExampleMatcher.matching;

/**
 * com.coupang.springframework.data.requery.repository.query.QueryByExampleBuilderTest
 *
 * @author debop
 * @since 18. 6. 19
 */
public class QueryByExampleBuilderTest extends AbstractDomainTest {

    @Before
    public void setup() {
        requeryTemplate.deleteAll(BasicUser.class);
    }

    @Test
    public void nameEqualExample() {

        BasicUser user = RandomData.randomUser();
        user.setName("example");
        requeryTemplate.insert(user);

        BasicUser exampleUser = new BasicUser();
        exampleUser.setName(user.getName());

        Return<? extends Result<BasicUser>> query = buildQueryByExample(Example.of(exampleUser));

        BasicUser foundUser = query.get().firstOrNull();
        assertThat(foundUser).isNotNull().isEqualTo(user);
    }

    @Test
    public void nameAndEmailEqExample() {

        BasicUser user = RandomData.randomUser();
        user.setName("example");
        user.setEmail("debop@example.com");
        requeryTemplate.insert(user);

        BasicUser exampleUser = new BasicUser();
        exampleUser.setName(user.getName());
        exampleUser.setEmail(user.getEmail());

        Return<? extends Result<BasicUser>> query = buildQueryByExample(Example.of(exampleUser));

        BasicUser foundUser = query.get().firstOrNull();
        assertThat(foundUser).isNotNull().isEqualTo(user);
    }

    @Test
    public void nameStartWithExample() {
        BasicUser user = RandomData.randomUser();
        user.setName("example");
        requeryTemplate.insert(user);

        BasicUser exampleUser = new BasicUser();
        exampleUser.setName("EXA");

        ExampleMatcher matcher = matching().withMatcher("name",
                                                        GenericPropertyMatcher.of(StringMatcher.STARTING, true));
        Example<BasicUser> example = Example.of(exampleUser, matcher);

        Return<? extends Result<BasicUser>> query = buildQueryByExample(example);

        BasicUser foundUser = query.get().firstOrNull();
        assertThat(foundUser).isNotNull().isEqualTo(user);
    }

    @SuppressWarnings("unchecked")
    private WhereAndOr<? extends Result<BasicUser>> buildQueryByExample(Example<BasicUser> example) {
        QueryElement<? extends Result<BasicUser>> root = (QueryElement<? extends Result<BasicUser>>) requeryTemplate.select(BasicUser.class);
        return QueryByExampleBuilder.getWhereAndOr(root, example);
    }
}
