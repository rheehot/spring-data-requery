package com.coupang.springframework.data.requery.util

import javax.persistence.metamodel.EntityType
import javax.persistence.metamodel.Metamodel
import javax.persistence.metamodel.SingularAttribute

/**
 * Wrapper around the Requery [Metamodel]
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
class RequeryMetamodel(private val metamodel: Metamodel) {

    private val managedTypes: Collection<Class<*>> by lazy {
        metamodel.managedTypes.mapNotNull { it.javaType }.toSet()
    }

    /**
     * Return whether the given type is managed by the backing Requery [Metamodel]
     */
    fun isRequeryManaged(type: Class<*>): Boolean {
        return managedTypes.contains(type)
    }

    /**
     * Returns whether the attribute of given name and type is the single identifier attribute of the given entity.
     */
    fun isSingleIdAttribute(entity: Class<*>, name: String, attributeType: Class<*>): Boolean {
        return metamodel.entities
            .filter { it.javaType == entity }
            .mapNotNull { getSingularIdAttribute(it) }
            .any { it.javaType == attributeType && it.name == name }
    }

    private fun getSingularIdAttribute(entityType: EntityType<*>): SingularAttribute<*, *>? {
        if(!entityType.hasSingleIdAttribute())
            return null

        return entityType.singularAttributes
            .first { it.isId }
    }

}