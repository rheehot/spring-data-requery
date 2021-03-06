package org.springframework.data.requery.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.data.requery.configs.RequeryTestConfiguration;
import org.springframework.data.requery.domain.sample.Role;
import org.springframework.data.requery.repository.config.EnableRequeryRepositories;
import org.springframework.data.requery.repository.sample.RoleRepository;
import org.springframework.data.requery.repository.support.SimpleRequeryRepository;
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
@EnableRequeryRepositories(basePackageClasses = { RoleRepository.class })
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

        repository.deleteAll();

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

    @Test
    public void shouldUseExplicitlyConfiguredEntityNameInDerivedCountQueries() {
        Role reference = new Role("ADMIN");
        repository.save(reference);

        assertThat(repository.countByName(reference.getName())).isEqualTo(1L);
    }
}
