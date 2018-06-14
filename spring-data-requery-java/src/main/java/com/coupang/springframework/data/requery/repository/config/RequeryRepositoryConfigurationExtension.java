package com.coupang.springframework.data.requery.repository.config;

import com.coupang.springframework.data.requery.repository.RequeryRepository;
import com.coupang.springframework.data.requery.repository.support.RequeryRepositoryFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

/**
 * com.coupang.springframework.data.requery.repository.config.RequeryRepositoryConfigurationExtension
 *
 * @author debop
 * @since 18. 6. 6
 */
@Slf4j
public class RequeryRepositoryConfigurationExtension extends RepositoryConfigurationExtensionSupport {

    @NotNull
    @Override
    public String getModuleName() {
        return "REQUERY";
    }

    @NotNull
    @Override
    public String getRepositoryFactoryBeanClassName() {
        return RequeryRepositoryFactoryBean.class.getName();
    }

    @NotNull
    @Override
    protected String getModulePrefix() {
        return getModuleName().toLowerCase(Locale.US);
    }

    @NotNull
    @Override
    protected Collection<Class<? extends Annotation>> getIdentifyingAnnotations() {
        return Arrays.asList(io.requery.Entity.class,
                             io.requery.Superclass.class);
    }

    @NotNull
    @Override
    protected Collection<Class<?>> getIdentifyingTypes() {
        return Collections.singleton(RequeryRepository.class);
    }

}
