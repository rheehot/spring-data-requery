package org.springframework.data.requery.repository;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.requery.domain.sample.ConcreteType1;
import org.springframework.data.requery.domain.sample.ConcreteType2;
import org.springframework.data.requery.repository.sample.ConcreteRepository1;
import org.springframework.data.requery.repository.sample.ConcreteRepository2;
import org.springframework.data.requery.repository.sample.SampleConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * MappedTypeRepositoryIntegrationTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 28
 */
@Transactional
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { SampleConfig.class })
public class MappedTypeRepositoryIntegrationTest {

    @Autowired ConcreteRepository1 concreteRepository1;
    @Autowired ConcreteRepository2 concreteRepository2;

    @Ignore("Raw Query구문에 Entity의 Table명을 추론하는 기능은 지원하지 않습니다.")
    @Test
    public void supportForExpressionBasedQueryMethods() {

        concreteRepository1.save(new ConcreteType1("foo"));
        concreteRepository2.save(new ConcreteType2("foo"));

        List<ConcreteType1> concretes1 = concreteRepository1.findAllByAttribute1("foo");
        List<ConcreteType2> concretes2 = concreteRepository2.findAllByAttribute1("foo");

        assertThat(concretes1).hasSize(1);
        assertThat(concretes2).hasSize(1);
    }

    @Ignore("Raw Query구문에 Entity의 Table명을 추론하는 기능은 지원하지 않습니다.")
    @Test
    public void supportForPaginationCustomQueryMethodsWithEntityExpression() {

        concreteRepository1.save(new ConcreteType1("foo"));
        concreteRepository2.save(new ConcreteType2("foo"));

        Page<ConcreteType2> page = concreteRepository2
            .findByAttribute1Custom("foo",
                                    PageRequest.of(0, 10, Sort.Direction.DESC, "attribute1"));

        assertThat(page.getNumberOfElements()).isEqualTo(1);
    }
}
