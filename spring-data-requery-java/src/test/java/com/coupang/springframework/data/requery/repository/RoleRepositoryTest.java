package com.coupang.springframework.data.requery.repository;

import com.coupang.springframework.data.requery.configs.RequeryTestConfiguration;
import com.coupang.springframework.data.requery.domain.sample.Role;
import com.coupang.springframework.data.requery.repository.config.EnableRequeryRepositories;
import com.coupang.springframework.data.requery.repository.sample.RoleRepository;
import com.coupang.springframework.data.requery.repository.sample.UserRepository;
import com.coupang.springframework.data.requery.repository.support.SimpleRequeryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@SuppressWarnings("unchecked")
@Transactional
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { RequeryTestConfiguration.class })
@EnableRequeryRepositories(basePackageClasses = { UserRepository.class })
public class RoleRepositoryTest {

    @Inject RoleRepository repository;

    @Test
    public void instancingRepository() {
        assertThat(repository).isNotNull();
        // log.debug("Repository type={}", AopUtils.getTargetClass(repository));
        assertThat(AopUtils.getTargetClass(repository)).isEqualTo(SimpleRequeryRepository.class);
    }

    @Test
    public void createsRole() throws Exception {

        Role reference = new Role("ADMIN");
        Role result = repository.save(reference);

        assertThat(result).isEqualTo(reference);
    }

    @Test
    public void updatesRole() throws Exception {

        Role reference = new Role("ADMIN");
        Role result = repository.save(reference);

        assertThat(result).isEqualTo(reference);

        reference.setName("USER");
        repository.save(reference);

        assertThat(repository.findById(result.getId())).isEqualTo(Optional.of(reference));
    }

    @Test
    public void shouldUseImplicitCountQuery() {
        Role reference = new Role("ADMIN");
        repository.save(reference);

        assertThat(repository.count()).isEqualTo(1L);
    }

    @Test
    public void shouldUseImplicitExistsQueries() {
        Role reference = new Role("ADMIN");
        repository.save(reference);

        assertThat(repository.existsById(reference.getId())).isTrue();
    }

    // TODO: PartTreeRequeryQuery 에서 문제가 발생한다.
    @Test
    @Ignore
    public void shouldUseExplicitlyConfiguredEntityNameInDerivedCountQueries() {
        Role reference = new Role("ADMIN");
        repository.save(reference);

        assertThat(repository.countByName(reference.getName())).isEqualTo(1L);
    }
}
