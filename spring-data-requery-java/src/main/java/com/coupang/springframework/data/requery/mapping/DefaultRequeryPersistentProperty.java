package com.coupang.springframework.data.requery.mapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.model.*;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * DefaultRequeryPersistentProperty
 *
 * @author debop@coupang.com
 * @since 18. 6. 8
 */
@Slf4j
public class DefaultRequeryPersistentProperty
    extends AnnotationBasedPersistentProperty<RequeryPersistentProperty>
    implements RequeryPersistentProperty {

    private final FieldNamingStrategy fieldNamingStrategy;

    /**
     * Creates a new {@link AnnotationBasedPersistentProperty}.
     */
    public DefaultRequeryPersistentProperty(Property property,
                                            PersistentEntity<?, RequeryPersistentProperty> owner,
                                            SimpleTypeHolder simpleTypeHolder,
                                            FieldNamingStrategy fieldNamingStrategy) {
        super(property, owner, simpleTypeHolder);
        this.fieldNamingStrategy = (fieldNamingStrategy != null) ? fieldNamingStrategy : PropertyNameFieldNamingStrategy.INSTANCE;
    }

    @Override
    protected Association<RequeryPersistentProperty> createAssociation() {
        return new Association<>(this, null);
    }

    @Override
    public boolean isIdProperty() {
        return findAnnotation(io.requery.Key.class) != null;
    }

    @Override
    public boolean isEmbedded() {
        return findAnnotation(io.requery.Embedded.class) != null;
    }

    @Override
    public boolean hasIndex() {
        return findAnnotation(io.requery.Index.class) != null;
    }

    @Override
    public String getFieldName() {
        final String fieldName;
        if (isIdProperty()) {
            fieldName = getProperty().getName();
        } else {
            fieldName = getAnnotatedColumnName().orElse(fieldNamingStrategy.getFieldName(this));
        }
        log.debug("property={}, fieldName={}", getProperty(), fieldName);
        return fieldName;
    }

    private Optional<String> getAnnotatedColumnName() {
        return Optional.ofNullable(findAnnotation(io.requery.Column.class))
            .map(name -> StringUtils.hasText(name.value()) ? name.value() : null);
    }


}
