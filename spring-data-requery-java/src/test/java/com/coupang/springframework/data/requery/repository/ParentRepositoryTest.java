package com.coupang.springframework.data.requery.repository;

import com.coupang.springframework.data.requery.configs.RequeryTestConfiguration;
import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.domain.sample.Child;
import com.coupang.springframework.data.requery.domain.sample.Parent;
import com.coupang.springframework.data.requery.domain.sample.Parent_Child;
import com.coupang.springframework.data.requery.repository.config.EnableRequeryRepositories;
import com.coupang.springframework.data.requery.repository.sample.ParentRepository;
import com.coupang.springframework.data.requery.repository.sample.UserRepository;
import io.requery.query.Result;
import io.requery.query.element.QueryElement;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static com.coupang.springframework.data.requery.utils.RequeryUtils.unwrap;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SuppressWarnings("unchecked")
@Transactional
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { RequeryTestConfiguration.class })
@EnableRequeryRepositories(basePackageClasses = { UserRepository.class })
public class ParentRepositoryTest {

    @Autowired ParentRepository repository;

    @Before
    public void setup() {
        repository.save(new Parent().addChild(new Child()));
        repository.save(new Parent().addChild(new Child()).addChild(new Child()));
        repository.save(new Parent().addChild(new Child()));
        repository.save(new Parent());
    }

    @Test
    public void testWithJoin() throws Exception {

        RequeryOperations ops = repository.getOperations();

        QueryElement<?> query = unwrap(ops
                                           .select(Parent.class)
                                           .distinct()
                                           .join(Parent_Child.class).on(Parent.ID.eq(Parent_Child.PARENT_ID)));

        Page<Parent> page = repository.findAll((QueryElement<? extends Result<Parent>>) query,
                                               PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "id")));

        assertThat(page).isNotNull();
        for (Parent parent : page.getContent()) {
            log.debug("Parent={}", parent);
        }

        assertThat(page.getSize()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(1);
    }
}
