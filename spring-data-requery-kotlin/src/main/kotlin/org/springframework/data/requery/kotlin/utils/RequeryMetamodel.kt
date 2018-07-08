package org.springframework.data.requery.utils

import io.requery.meta.EntityModel

/**
 * RequeryMetamodel
 *
 * @author debop@coupang.com
 */
class RequeryMetamodel(val entityModel: EntityModel) {

    private val managedTypes = arrayListOf<Class<*>>()

    fun isRequeryManaged(entityClass: Class<*>): Boolean {
        return getManagedTypes().contains(entityClass)
    }

    fun isSingleIdAttribute(entityClass: Class<*>, name: String, attributeClass: Class<*>): Boolean {

        return entityModel.types
                   .filter { it.classType == entityClass }
                   .mapNotNull { it.singleKeyAttribute }
                   .filter { it.classType == attributeClass }
                   .map { it.name == name }
                   .firstOrNull()
               ?: false

    }

    private fun getManagedTypes(): Collection<Class<*>> {
        if(managedTypes.isEmpty()) {
            managedTypes.addAll(entityModel.types.mapNotNull { it.classType })
        }
        return managedTypes
    }

}