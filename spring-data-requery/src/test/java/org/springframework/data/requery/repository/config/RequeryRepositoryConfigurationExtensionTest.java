package org.springframework.data.requery.repository.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;
import org.springframework.data.repository.config.RepositoryConfigurationSource;
import org.springframework.data.requery.repository.support.RequeryRepositoryFactoryBean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * RequeryRepositoryConfigurationExtensionTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 12
 */
@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class RequeryRepositoryConfigurationExtensionTest {

    @Mock RepositoryConfigurationSource configSource;

    public @Rule ExpectedException exception = ExpectedException.none();

    @Test
    public void requeryRepositoryClasses() {

        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        RepositoryConfigurationExtension extension = new RequeryRepositoryConfigurationExtension();
        extension.registerBeansForRoot(factory, configSource);

        assertThat(extension.getModuleName()).isEqualTo("REQUERY");
        assertThat(extension.getRepositoryFactoryBeanClassName()).isEqualTo(RequeryRepositoryFactoryBean.class.getName());
    }
}
