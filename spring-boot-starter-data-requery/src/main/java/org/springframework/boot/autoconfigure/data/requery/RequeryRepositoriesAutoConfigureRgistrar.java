package org.springframework.boot.autoconfigure.data.requery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;
import org.springframework.data.requery.repository.config.EnableRequeryRepositories;
import org.springframework.data.requery.repository.config.RequeryRepositoryConfigurationExtension;

import java.lang.annotation.Annotation;

/**
 * org.springframework.boot.autoconfigure.data.requery.RequeryRepositoriesAutoConfigureRgistrar
 *
 * @author debop
 */
public class RequeryRepositoriesAutoConfigureRgistrar
    extends AbstractRepositoryConfigurationSourceSupport {

    private static final Logger log = LoggerFactory.getLogger(RequeryRepositoriesAutoConfigureRgistrar.class);

    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return EnableRequeryRepositories.class;
    }

    @Override
    protected Class<?> getConfiguration() {
        return EnableRequeryRepositoriesConfiguration.class;
    }

    @Override
    protected RepositoryConfigurationExtension getRepositoryConfigurationExtension() {
        return new RequeryRepositoryConfigurationExtension();
    }

    @EnableRequeryRepositories
    private static class EnableRequeryRepositoriesConfiguration {

    }
}
