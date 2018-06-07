package com.coupang.springframework.data.requery.repository.support;

import com.coupang.springframework.data.requery.utils.RequeryMetamodel;
import io.requery.meta.Attribute;
import io.requery.meta.EntityModel;
import io.requery.meta.Type;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * RequeryEntityModelEntityInformation
 *
 * @author debop@coupang.com
 * @since 18. 6. 7
 */
@Slf4j
public class RequeryEntityModelEntityInformation<T, ID> extends RequeryEntityInformationSupport<T, ID> {

    private final IdMetadata<T> idMetadata;
    private final Optional<Attribute<? super T, ?>> versionAttribute;
    private final EntityModel entityModel;
    @Nullable private final String entityName;


    public RequeryEntityModelEntityInformation(@NotNull Class<T> domainClass, @NotNull EntityModel entityModel) {
        super(domainClass);

        this.entityModel = entityModel;

        Type<T> type = entityModel.typeOf(domainClass);
        if (type == null) {
            throw new IllegalArgumentException("The given domain class can not be found in the given EntityModel!");
        }

        this.entityName = type.getName();

        if (type.getKeyAttributes().isEmpty()) {
            throw new IllegalArgumentException("The given domain class does not contains an id attribute!");
        }

        this.idMetadata = new IdMetadata<>(type);
        this.versionAttribute = findVersionAttribute(type, entityModel);
    }

    private static <T> Optional<Attribute<? super T, ?>> findVersionAttribute(Type<T> type, EntityModel entityModel) {

        log.debug("Find version attribute, type={}", type);

        for (Attribute<T, ?> attr : type.getAttributes()) {
            if (attr.isVersion()) {
                return Optional.of(attr);
            }
        }

        Class<?> baseClass = type.getBaseType();
        try {
            Type<?> baseType = entityModel.typeOf(baseClass);
            if (baseType.getKeyAttributes().isEmpty()) {
                return Optional.empty();
            }
            return findVersionAttribute((Type<T>) baseType, entityModel);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }

    }


    @Override
    public String getEntityName() {
        return (entityName != null) ? entityName : super.getEntityName();
    }

    @Override
    public ID getId(T entity) {
        log.trace("Get id value. entity={}", entity);

        BeanWrapper entityWrapper = new DirectFieldAccessFallbackBeanWrapper(entity);
        if (idMetadata.hasSimpleId()) {
            return (ID) entityWrapper.getPropertyValue(idMetadata.getSimpleIdAttribute().getName());
        }

        BeanWrapper idWrapper = new IdentifierDerivingDirectFieldAccessFallbackBeanWrapper(idMetadata.getType(), entityModel);
        boolean partialValueFound = false;

        for (Attribute<? super T, ?> attribute : idMetadata) {
            Object propertyValue = entityWrapper.getPropertyValue(attribute.getName());

            if (propertyValue != null) {
                partialValueFound = true;
            }
            idWrapper.setPropertyValue(attribute.getName(), propertyValue);
        }

        return partialValueFound ? (ID) idWrapper.getWrappedInstance() : null;

    }

    @Override
    public Class<ID> getIdType() {
        return (Class<ID>) idMetadata.getType();
    }

    @Nullable
    @Override
    public Attribute<? super T, ?> getIdAttribute() {
        return idMetadata.getSimpleIdAttribute();
    }

    @Override
    public boolean hasCompositeId() {
        return !idMetadata.hasSimpleId();
    }

    @Override
    public Iterable<String> getIdAttributeNames() {
        return idMetadata.attributes
            .stream()
            .map(Attribute::getName)
            .collect(Collectors.toList());
    }

    @Nullable
    @Override
    public Object getCompositeIdAttributeValue(Object id, String idAttribute) {
        Assert.isTrue(hasCompositeId(), "Model must have a composite Id!");

        return new DirectFieldAccessFallbackBeanWrapper(id).getPropertyValue(idAttribute);
    }

    @Override
    public boolean isNew(T entity) {
        if (!versionAttribute.isPresent() ||
            versionAttribute.map(Attribute::getClassType).map(Class::isPrimitive).orElse(false)) {
            return super.isNew(entity);
        }

        BeanWrapper wrapper = new DirectFieldAccessFallbackBeanWrapper(entity);

        return versionAttribute.map(it -> wrapper.getPropertyValue(it.getName()) == null).orElse(true);
    }

    private static class IdMetadata<T> implements Iterable<Attribute<? super T, ?>> {

        private final Type<T> domainType;
        private final Set<Attribute<? super T, ?>> attributes;
        @Nullable
        private Class<?> idType;

        public IdMetadata(@NotNull Type<T> source) {
            this.domainType = source;
            if (source.getKeyAttributes().size() == 1) {
                this.attributes = Collections.singleton(source.getSingleKeyAttribute());
            } else {
                this.attributes = Collections.unmodifiableSet(source.getKeyAttributes());
            }

        }

        public boolean hasSimpleId() {
            return attributes.size() == 1;
        }

        public Class<?> getType() {
            if (idType != null) {
                return idType;
            }
            // lazy initialization of idType field with tolerable benign data-race
            this.idType = tryExtractIdTypeWithFallbackToIdTypeLookup();

            if (this.idType == null) {
                throw new IllegalArgumentException("Cannot resolve Id type from " + domainType);
            }
            return this.idType;
        }

        @Nullable
        private Class<?> tryExtractIdTypeWithFallbackToIdTypeLookup() {
            try {
                Attribute<T, ?> keyAttr = domainType.getSingleKeyAttribute();
                return keyAttr.getClassType();
            } catch (IllegalStateException e) {
                return null;
            }
        }

        public Attribute<? super T, ?> getSimpleIdAttribute() {
            return attributes.iterator().next();
        }

        public Iterator<Attribute<? super T, ?>> iterator() {
            return attributes.iterator();
        }

    }

    private static class IdentifierDerivingDirectFieldAccessFallbackBeanWrapper extends DirectFieldAccessFallbackBeanWrapper {

        private final EntityModel entityModel;
        private final RequeryMetamodel requeryMetamodel;

        IdentifierDerivingDirectFieldAccessFallbackBeanWrapper(Class<?> domainType, EntityModel entityModel) {
            super(domainType);
            this.entityModel = entityModel;
            this.requeryMetamodel = new RequeryMetamodel(entityModel);
        }

        /**
         * In addition to the functionality described in {@link BeanWrapperImpl} it is checked whether we have a nested
         * entity that is part of the id key. If this is the case, we need to derive the identifier of the nested entity.
         */
        @Override
        public void setPropertyValue(String propertyName, Object value) {
            if (!isIdentifierDerivationNecessary(value)) {
                super.setPropertyValue(propertyName, value);
                return;
            }

            // Derive the identifier from the nested entity that is part of the composite key.
            RequeryEntityModelEntityInformation nestedEntityInformation =
                new RequeryEntityModelEntityInformation(value.getClass(), this.entityModel);

            Object nestedIdPropertyValue = new DirectFieldAccessFallbackBeanWrapper(value)
                .getPropertyValue(nestedEntityInformation.getRequiredIdAttribute().getName());
            super.setPropertyValue(propertyName, nestedIdPropertyValue);
        }


        @Nullable
        private Object extractActualIdPropertyValue(@NotNull BeanWrapper sourceIdValueWrapper, String idAttributeName) {

            Object idPropertyValue = sourceIdValueWrapper.getPropertyValue(idAttributeName);

            if (idPropertyValue != null) {
                Class<?> idPropertyValueClass = idPropertyValue.getClass();

                if (requeryMetamodel.isRequeryManaged(idPropertyValueClass)) {
                    return idPropertyValue;
                }

                return new DirectFieldAccessFallbackBeanWrapper(idPropertyValueClass)
                    .getPropertyValue(tryFindSingularIdAttributeNameOrUseFallback(idPropertyValueClass, idAttributeName));
            }

            return null;
        }

        private String tryFindSingularIdAttributeNameOrUseFallback(Class<?> idPropertyValueType,
                                                                   String fallbackIdTypePropertyName) {
            log.debug("idPropertyValueType={}, fallbackIdTypePropertyName={}", idPropertyValueType, fallbackIdTypePropertyName);
            Type<?> idPropertyType = entityModel.typeOf(idPropertyValueType);
            for (Attribute<?, ?> attr : idPropertyType.getAttributes()) {
                if (attr.isKey()) {
                    log.debug("Found attribute. attr name={}, propertyName={}", attr.getName(), attr.getPropertyName());
                    return attr.getName();
                }
            }
            return fallbackIdTypePropertyName;
        }

        private boolean isIdentifierDerivationNecessary(@Nullable Object value) {
            if (value == null) {
                return false;
            }

            try {
                Type<?> entityType = this.entityModel.typeOf(value.getClass());
                return entityType != null && !entityType.getKeyAttributes().isEmpty();
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
    }
}
