package org.springframework.data.requery.mapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.data.mapping.model.BasicPersistentEntity;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * DefaultRequeryPersistentEntity
 *
 * @author debop@coupang.com
 * @since 18. 6. 8
 */
@Slf4j
public class DefaultRequeryPersistentEntity<T>
    extends BasicPersistentEntity<T, RequeryPersistentProperty>
    implements RequeryPersistentEntity<T> {


    private String name;
    private final List<RequeryPersistentProperty> idProperties = new ArrayList<>();
    private final List<RequeryPersistentProperty> embeddedProperties = new ArrayList<>();
    private final List<RequeryPersistentProperty> indexProperties = new ArrayList<>();

    private final Map<Class<? extends Annotation>, Optional<Annotation>> annotationCache;
    private final Map<Class<? extends Annotation>, Set<? extends Annotation>> repeatableAnnotationCache;

    public DefaultRequeryPersistentEntity(final TypeInformation<T> information) {
        super(information);

        this.name = information.getType().getSimpleName();
        annotationCache = new LinkedHashMap<>();
        repeatableAnnotationCache = new LinkedHashMap<>();

        final io.requery.Entity entity = findAnnotation(io.requery.Entity.class);

        if (entity != null) {
            name = StringUtils.hasText(entity.name()) ? entity.name() : name;
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // Nothing to do.
    }

    @Override
    public void addPersistentProperty(RequeryPersistentProperty property) {
        super.addPersistentProperty(property);

        if (property.isIdProperty()) {
            idProperties.add(property);
        }
        if (property.isEmbedded()) {
            embeddedProperties.add(property);
        }
        if (property.hasIndex()) {
            indexProperties.add(property);
        }
    }

    @Override
    public RequeryPersistentProperty getSingleIdProperty() {
        return idProperties.isEmpty() ? null : idProperties.get(0);
    }

    @Override
    public Collection<RequeryPersistentProperty> getIdProperties() {
        return Collections.unmodifiableList(idProperties);
    }

    @Override
    public Collection<RequeryPersistentProperty> getIndexes() {
        return Collections.unmodifiableList(indexProperties);
    }

    @Override
    public Collection<RequeryPersistentProperty> getEmbeddedProperties() {
        return Collections.unmodifiableList(embeddedProperties);
    }


    @Override
    public <A extends Annotation> A findAnnotation(Class<A> annotationType) {
        return (A) annotationCache
            .computeIfAbsent(annotationType,
                             it -> Optional.ofNullable(AnnotatedElementUtils.findMergedAnnotation(getType(), it)))
            .orElse(null);
    }

    public <A extends Annotation> Set<A> findAnnotations(Class<A> annotationType) {
        return (Set<A>) repeatableAnnotationCache
            .computeIfAbsent(annotationType,
                             it -> AnnotatedElementUtils.findMergedRepeatableAnnotations(getType(), it));
    }
}
