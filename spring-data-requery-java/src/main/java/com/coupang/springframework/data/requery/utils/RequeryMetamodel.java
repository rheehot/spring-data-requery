package com.coupang.springframework.data.requery.utils;

import io.requery.meta.Attribute;
import io.requery.meta.EntityModel;
import io.requery.meta.Type;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * RequeryMetamodel
 *
 * @author debop@coupang.com
 * @since 18. 6. 7
 */
public class RequeryMetamodel {

    private final EntityModel entityModel;
    private Optional<Collection<Class<?>>> managedTypes = Optional.empty();


    public RequeryMetamodel(@NotNull EntityModel entityModel) {
        this.entityModel = entityModel;
    }

    public boolean isRequeryManaged(@NotNull Class<?> entityClass) {
        return getManagedTypes().contains(entityClass);
    }

    public boolean isSingleIdAttribute(@NotNull final Class<?> entityClass,
                                       @NotNull final String name,
                                       @NotNull final Class<?> attributeClass) {
        return entityModel.getTypes()
            .stream()
            .filter(type -> entityClass.equals(type.getClassType()))
            .findFirst()
            .flatMap(type -> getSingleKeyAttribute(type))
            .filter(attr -> attr.getClassType().equals(attributeClass))
            .map(attr -> attr.getName().equals(name))
            .orElse(false);

    }

    @NotNull
    private Collection<Class<?>> getManagedTypes() {
        if (!managedTypes.isPresent()) {
            Set<Type<?>> managedTypes = entityModel.getTypes();
            Set<Class<?>> types = new HashSet<>(managedTypes.size());

            for (Type<?> managedType : managedTypes) {
                Class<?> type = managedType.getClassType();
                if (type != null) {
                    types.add(type);
                }
            }
            this.managedTypes = Optional.of(types);
        }

        return this.managedTypes.get();
    }


    private static Optional<? extends Attribute<?, ?>> getSingleKeyAttribute(Type<?> type) {
        return Optional.ofNullable(type.getSingleKeyAttribute());
    }

}
