package com.coupang.springframework.data.requery.repository.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

import java.lang.annotation.Annotation;

/**
 * com.coupang.springframework.data.requery.repository.config.RequeryRepositoriesRegistar
 *
 * @author debop
 * @since 18. 6. 6
 */
public class RequeryRepositoriesRegistar extends RepositoryBeanDefinitionRegistrarSupport {

    @NotNull
    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return EnableRequeryRepositories.class;
    }

    @NotNull
    @Override
    protected RepositoryConfigurationExtension getExtension() {
        return new RequeryRepositoryConfigExtension();
    }

}
