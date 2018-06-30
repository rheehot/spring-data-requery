package com.coupang.springframework.data.requery.repository;

import com.coupang.springframework.data.requery.configs.RequeryTestConfiguration;
import com.coupang.springframework.data.requery.domain.sample.Role;
import com.coupang.springframework.data.requery.domain.sample.User;
import com.coupang.springframework.data.requery.repository.config.EnableRequeryRepositories;
import com.coupang.springframework.data.requery.repository.sample.RoleRepository;
import com.coupang.springframework.data.requery.repository.sample.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for executing finders, thus testing various query lookup strategies.
 *
 * @author debop
 * @since 18. 6. 30
 */
@RunWith(SpringRunner.class)
@ContextConfiguration
@Transactional
public class UserRepositoryFinderTest {

    @Configuration
    @EnableRequeryRepositories(basePackageClasses = { UserRepository.class })
    static class TestConfiguration extends RequeryTestConfiguration {

    }

    @Autowired UserRepository userRepository;
    @Autowired RoleRepository roleRepository;

    User dave, carter, oliver;
    Role drummer, guitarist, singer;


    private static User createUser() {
        return createUser(null, null, null);
    }

    private static User createUser(String firstname, String lastname, String email, Role... roles) {
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmailAddress(email);
        user.setActive(true);
        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        user.getRoles().addAll(Arrays.asList(roles));

        return user;
    }

    @Before
    public void setup() {

        drummer = roleRepository.save(new Role("DRUMMER"));
        guitarist = roleRepository.save(new Role("GUITARIST"));
        singer = roleRepository.save(new Role("SINGER"));

        dave = userRepository.save(createUser("Dave", "Matthews", "dave@dmband.com", singer));
        carter = userRepository.save(createUser("Carter", "Beauford", "carter@dmband.com", singer, drummer));
        oliver = userRepository.save(createUser("Oliver Auguest", "Matthews", "oliver@dmband.com"));
    }

    @After
    public void clearUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void testSimpleCustomCreatedFinder() {

        User user = userRepository.findByEmailAddressAndLastname("dave@dmband.com", "Matthews");
        assertThat(user).isEqualTo(dave);
    }

    @Test
    public void returnsNullIfNothingFound() {

        User user = userRepository.findByEmailAddress("foobar");
        assertThat(user).isNull();
    }

    /**
     * Tests creation of a simple query consisting of {@code AND} and {@code OR} parts.
     */
    @Test
    public void testAndOrFinder() {

        List<User> users = userRepository.findByEmailAddressAndLastnameOrFirstname("dave@dmband.com",
                                                                                   "Matthews",
                                                                                   "Carter");
        assertThat(users)
            .hasSize(2)
            .containsOnly(dave, carter);
    }
}
