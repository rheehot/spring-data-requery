package com.coupang.springframework.data.requery.repository.query;

import io.requery.Entity;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Default implementation for {@link RequeryEntityMetadata}.
 *
 * @author debop@coupang.com
 * @since 18. 6. 7
 */
@Slf4j
public class DefaultRequeryEntityMetadata<T> implements RequeryEntityMetadata<T> {

    @NotNull
    public static <T> DefaultRequeryEntityMetadata<T> of(@NotNull Class<T> domainClass) {
        Assert.notNull(domainClass, "domainClass must not be null");
        return new DefaultRequeryEntityMetadata<>(domainClass);
    }

    private final Class<T> domainClass;

    public DefaultRequeryEntityMetadata(@NotNull Class<T> domainClass) {
        Assert.notNull(domainClass, "domainClass must not be null!");
        this.domainClass = domainClass;
    }

    @Override
    public String getEntityName() {
        Entity entity = AnnotatedElementUtils.findMergedAnnotation(domainClass, Entity.class);

        log.debug("Get entity name... domainClass={}, entity={}", domainClass.getName(), entity);

        return (entity != null) && StringUtils.hasText(entity.name())
               ? entity.name()
               : domainClass.getSimpleName();
    }

    @Override
    public String getModelName() {
        Entity entity = AnnotatedElementUtils.findMergedAnnotation(domainClass, Entity.class);

        log.debug("Get model name... domainClass={}, entity={}", domainClass.getName(), entity);

        return (entity != null) && StringUtils.hasText(entity.model())
               ? entity.model()
               : "default";
    }

    @Override
    public Class<T> getJavaType() {
        return domainClass;
    }
}

