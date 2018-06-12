package com.coupang.springframework.data.requery.java.repository.config;

import com.coupang.springframework.data.requery.repository.config.RequeryRepositoryConfigExtension;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;
import org.springframework.data.repository.config.RepositoryConfigurationSource;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * RequeryRepositoryConfigExtensionTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 12
 */
@RunWith(MockitoJUnitRunner.class)
public class RequeryRepositoryConfigExtensionTest {

    @Mock RepositoryConfigurationSource configSource;

    public @Rule ExpectedException exception = ExpectedException.none();

    @Test
    public void registerDefaultBeanPostProcessorsByDefault() {

        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();

        RepositoryConfigurationExtension extension = new RequeryRepositoryConfigExtension();
        extension.registerBeansForRoot(factory, configSource);

        Iterable<String> names = Arrays.asList(factory.getBeanDefinitionNames());

        assertThat(names).contains(AnnotationConfigUtils.PERSISTENCE_ANNOTATION_PROCESSOR_BEAN_NAME);
    }
}
