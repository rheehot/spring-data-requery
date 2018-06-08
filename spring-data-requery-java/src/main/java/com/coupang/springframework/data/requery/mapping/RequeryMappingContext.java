package com.coupang.springframework.data.requery.mapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mapping.context.AbstractMappingContext;
import org.springframework.data.mapping.model.FieldNamingStrategy;
import org.springframework.data.mapping.model.Property;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.util.TypeInformation;

import java.util.Optional;

/**
 * RequeryMappingContext
 *
 * @author debop@coupang.com
 * @since 18. 6. 8
 */
@Slf4j
public class RequeryMappingContext
    extends AbstractMappingContext<DefaultRequeryPersistentEntity<?>, RequeryPersistentProperty>
    implements ApplicationContextAware {

    private FieldNamingStrategy fieldNamingStrategy;
    private Optional<ApplicationContext> applicationContext;

    public RequeryMappingContext() {
        applicationContext = Optional.empty();
    }

    public void setFieldNamingStrategy(FieldNamingStrategy fieldNamingStrategy) {
        this.fieldNamingStrategy = fieldNamingStrategy;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = Optional.of(applicationContext);
    }

    @Override
    protected <T> DefaultRequeryPersistentEntity<?> createPersistentEntity(TypeInformation<T> typeInformation) {
        final DefaultRequeryPersistentEntity<T> entity = new DefaultRequeryPersistentEntity<>(typeInformation);
        applicationContext.ifPresent(entity::setApplicationContext);
        log.debug("Create persistent entity. typeInformation={}", typeInformation);
        return entity;
    }

    @Override
    protected RequeryPersistentProperty createPersistentProperty(Property property,
                                                                 DefaultRequeryPersistentEntity<?> owner,
                                                                 SimpleTypeHolder simpleTypeHolder) {
        log.debug("Create property. property={}", property);
        return new DefaultRequeryPersistentProperty(property,
                                                    owner,
                                                    simpleTypeHolder,
                                                    fieldNamingStrategy);
    }
}
