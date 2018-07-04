package org.springframework.data.requery.repository.support

import io.requery.meta.EntityModel

/**
 * org.springframework.data.requery.repository.support.RequeryMetamodel
 *
 * @author debop
 */
class RequeryMetamodel(val entityModel: EntityModel) {

    val managedTypes: Collection<Class<*>> by lazy {
        entityModel.types.mapNotNull { it.classType }
    }

    fun isRequeryManaged(domainClass: Class<out Any>): Boolean {
        return managedTypes.contains(domainClass)
    }

    fun isSingleIdAttribute(domainClass: Class<out Any>,
                            name: String,
                            attributeClass: Class<*>): Boolean {

        val type = entityModel.types.find { it.classType == domainClass }

        if(type != null) {
            val attr = type.singleKeyAttribute
            return attr.classType == attributeClass && attr.name == name
        }
        return false
    }
}