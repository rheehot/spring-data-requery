package com.coupang.springframework.data.requery.configs;

import io.requery.Entity;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * com.coupang.springframework.data.requery.configs.RequeryEntityClassScanner
 *
 * @author debop
 * @since 18. 6. 7
 */
public class RequeryEntityClassScanner {

    private static final Class<? extends Annotation> ENTITY_ANNOTATION = Entity.class;

    public static Set<Class<?>> scanForEntities(@NotNull final String... basePackages) throws ClassNotFoundException {

        Set<Class<?>> entities = new HashSet<>();

        for (String basePackage : basePackages) {
            entities.addAll(scanForEntities(basePackage));
        }
        return entities;
    }

    public static Set<Class<?>> scanForEntities(final String basePackage) throws ClassNotFoundException {

        Set<Class<?>> entities = new HashSet<>();

        if (StringUtils.hasText(basePackage)) {
            ClassPathScanningCandidateComponentProvider componentProvider =
                new ClassPathScanningCandidateComponentProvider(false);

            componentProvider.addIncludeFilter(new AnnotationTypeFilter(ENTITY_ANNOTATION));

            for (BeanDefinition definition : componentProvider.findCandidateComponents(basePackage)) {
                if (definition.getBeanClassName() != null) {
                    entities.add(ClassUtils.forName(definition.getBeanClassName(), null));
                }
            }
        }

        return entities;
    }
}
