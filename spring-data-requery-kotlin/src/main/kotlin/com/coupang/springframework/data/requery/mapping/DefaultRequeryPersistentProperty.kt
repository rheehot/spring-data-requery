package com.coupang.springframework.data.requery.mapping

import org.springframework.data.mapping.Association
import org.springframework.data.mapping.PersistentEntity
import org.springframework.data.mapping.model.*

/**
 * DefaultRequeryPersistentProperty
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
class DefaultRequeryPersistentProperty @JvmOverloads constructor(
    property: Property,
    owner: PersistentEntity<*, RequeryPersistentProperty>,
    typeHolder: SimpleTypeHolder,
    private val filedNamingStrategy: FieldNamingStrategy = PropertyNameFieldNamingStrategy.INSTANCE)
    : AnnotationBasedPersistentProperty<RequeryPersistentProperty>(property, owner, typeHolder), RequeryPersistentProperty {


    override fun createAssociation(): Association<RequeryPersistentProperty> {
        TODO("not implemented")
    }
}