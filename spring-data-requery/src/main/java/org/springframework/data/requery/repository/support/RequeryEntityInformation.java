package org.springframework.data.requery.repository.support;

import io.requery.meta.Attribute;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.requery.repository.query.RequeryEntityMetadata;

/**
 * RequeryEntityInformation
 *
 * @author debop@coupang.com
 * @since 18. 6. 7
 */
public interface RequeryEntityInformation<T, ID> extends EntityInformation<T, ID>, RequeryEntityMetadata<T> {

    /**
     * Returns the id attriute of the entity
     */
    @Nullable
    Attribute<? super T, ?> getIdAttribute();


    default Attribute<? super T, ?> getRequiredIdAttribute() throws IllegalArgumentException {
        Attribute<? super T, ?> id = getIdAttribute();
        if (id != null) {
            return id;
        }
        throw new IllegalArgumentException(
            String.format("Could not obtain required identifier attribute for type %s!", getEntityName()));
    }

    boolean hasCompositeId();


    Iterable<String> getIdAttributeNames();

    @Nullable
    Object getCompositeIdAttributeValue(Object id, String idAttribute);

}