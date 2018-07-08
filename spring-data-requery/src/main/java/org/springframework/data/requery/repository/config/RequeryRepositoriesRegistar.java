package org.springframework.data.requery.repository.config;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

import java.lang.annotation.Annotation;

/**
 * {@link ImportBeanDefinitionRegistrar} to enable {@link EnableRequeryRepositories} annotation.
 *
 * @author debop
 * @since 18. 6. 6
 */
@Slf4j
public class RequeryRepositoriesRegistar extends RepositoryBeanDefinitionRegistrarSupport {

    @NotNull
    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return EnableRequeryRepositories.class;
    }

    @NotNull
    @Override
    protected RepositoryConfigurationExtension getExtension() {
        return new RequeryRepositoryConfigurationExtension();
    }

}
