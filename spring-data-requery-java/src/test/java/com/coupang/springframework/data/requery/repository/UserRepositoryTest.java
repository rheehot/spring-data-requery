package com.coupang.springframework.data.requery.repository;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.domain.sample.Role;
import com.coupang.springframework.data.requery.domain.sample.User;
import com.coupang.springframework.data.requery.repository.config.EnableRequeryRepositories;
import com.coupang.springframework.data.requery.repository.config.InfrastructureConfig;
import com.coupang.springframework.data.requery.repository.sample.RoleRepository;
import com.coupang.springframework.data.requery.repository.sample.UserRepository;
import com.coupang.springframework.data.requery.repository.sample.UserRepositoryImpl;
import com.coupang.springframework.data.requery.repository.support.RequeryRepositoryFactoryBean;
import com.coupang.springframework.data.requery.repository.support.SimpleRequeryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * UserRepositoryTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 28
 */
@RunWith(SpringRunner.class)
@ContextConfiguration
public class UserRepositoryTest {

    @Configuration
    @EnableRequeryRepositories(basePackageClasses = { UserRepository.class })
    static class TestConfiguration extends InfrastructureConfig {

        @Autowired ApplicationContext applicationContext;
        @Autowired RequeryOperations operations;

        @Bean
        public UserRepository userRepository() {

            RequeryRepositoryFactoryBean<UserRepository, User, Integer> factory = new RequeryRepositoryFactoryBean<>(UserRepository.class);

            factory.setOperations(operations);
            factory.setBeanFactory(applicationContext);
            factory.setRepositoryBaseClass(SimpleRequeryRepository.class);

            factory.setCustomImplementation(new UserRepositoryImpl());

            factory.afterPropertiesSet();

            return factory.getObject();
        }

        @Bean
        public RoleRepository roleRepository() {
            RequeryRepositoryFactoryBean<RoleRepository, Role, Integer> factory = new RequeryRepositoryFactoryBean<>(RoleRepository.class);

            return factory.getObject();
        }
    }

    @Autowired RequeryOperations operations;

    // CUT
    @Autowired UserRepository repository;

    // Test fixture
    User firstUser, secondUser, thirdUser, fourthUser;
    Integer id;
    Role adminRole;

    private static User createUser(String firstname, String lastname, String email, Role... roles) {
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmailAddress(email);

        for (Role role : roles) {
            user.getRoles().add(role);
        }
        return user;
    }


    @Before
    public void setup() throws Exception {

        firstUser = createUser("Debop", "Bae", "debop@coupang.com");
        firstUser.setAge(51);

        secondUser = createUser("HyoungGab", "Ahn", "diego@coupang.com");
        secondUser.setAge(30);

        Thread.sleep(10);

        thirdUser = createUser("Jinie", "Park", "jinie@coupang.com");
        thirdUser.setAge(26);

        fourthUser = createUser("Nickoon", "Jeon", "nickoon@coupang.com");
        fourthUser.setAge(30);

        adminRole = new Role("admin");
    }

    @Test
    public void testCreation() {

        Integer before = operations.count(User.class).get().value();

        flushTestUsers();

        assertThat(operations.count(User.class).get().value()).isEqualTo(before + 4);
    }


    protected void flushTestUsers() {

        operations.upsert(adminRole);

        firstUser = repository.save(firstUser);
        secondUser = repository.save(secondUser);
        thirdUser = repository.save(thirdUser);
        fourthUser = repository.save(fourthUser);

        id = firstUser.getId();

        assertThat(id).isNotNull();
        assertThat(secondUser.getId()).isNotNull();
        assertThat(thirdUser.getId()).isNotNull();
        assertThat(fourthUser.getId()).isNotNull();

        assertThat(repository.existsById(id)).isTrue();
        assertThat(repository.existsById(secondUser.getId())).isTrue();
        assertThat(repository.existsById(thirdUser.getId())).isTrue();
        assertThat(repository.existsById(fourthUser.getId())).isTrue();
    }

}
