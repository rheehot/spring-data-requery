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

    private final Class<T> domainType;

    public DefaultRequeryEntityMetadata(@NotNull Class<T> domainType) {
        Assert.notNull(domainType, "domainType must not be null!");
        this.domainType = domainType;
    }

    @Override
    public String getEntityName() {
        Entity entity = AnnotatedElementUtils.findMergedAnnotation(domainType, Entity.class);

        log.debug("Get entity name... domainType={}, entity={}", domainType.getName(), entity);

        return (entity != null) && StringUtils.hasText(entity.name())
               ? entity.name()
               : domainType.getSimpleName();
    }

    @Override
    public String getModelName() {
        Entity entity = AnnotatedElementUtils.findMergedAnnotation(domainType, Entity.class);

        log.debug("Get model name... domainType={}, entity={}", domainType.getName(), entity);

        return (entity != null) && StringUtils.hasText(entity.model())
               ? entity.model()
               : "default";
    }

    @Override
    public Class<T> getJavaType() {
        return domainType;
    }
}

