package org.springframework.data.requery.kotlin

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.data.requery.kotlin.domain.AbstractDomainTest
import org.springframework.data.requery.kotlin.domain.sample.UserEntity

/**
 * org.springframework.data.requery.RequeryExtensionsTest
 *
 * @author debop
 */
class RequeryExtensionsTest: AbstractDomainTest() {

    @Test
    fun `retrieve Entity Context`() {
        val entityContext = dataStore.getEntityContext()
        assertThat(entityContext).isNotNull

        assertThat(operations.entityContext).isEqualTo(entityContext)
    }

    @Test
    fun `retrieve EntityModel`() {
        val entityModel = dataStore.getEntityModel()
        assertThat(entityModel).isNotNull
        assertThat(entityModel.name).isEqualTo("default")

        assertThat(operations.entityModel).isEqualTo(entityModel)
    }

    @Test
    fun `retrieve entity types from EntityDataStore`() {
        val types = dataStore.getEntityTypes()
        assertThat(types).isNotEmpty.contains(UserEntity.`$TYPE`)
    }

    @Test
    fun `retrieve entity classes from EntityDataStore`() {
        val classes = dataStore.getEntityClasses()
        assertThat(classes).isNotEmpty.contains(UserEntity::class.java)
    }

    @Test
    fun `retrieve type from EntityDataStore`() {
        assertThat(dataStore.getType(UserEntity::class.java)).isEqualTo(UserEntity.`$TYPE`)
        assertThat(dataStore.getType(UserEntity::class)).isEqualTo(UserEntity.`$TYPE`)
    }

    @Test
    fun `get Identifier attribute from entity`() {

        val keyAttrs = dataStore.getKeyAttributes(UserEntity::class.java)
        assertThat(keyAttrs).hasSize(1)
        assertThat(keyAttrs.first().classType).isEqualTo(Int::class.java)
    }

    @Test
    fun `get single id attribute from domain class`() {

        val keyAttr = dataStore.getSingleKeyAttribute(UserEntity::class.java)
        assertThat(keyAttr).isNotNull
        assertThat(keyAttr.classType).isEqualTo(Int::class.java)
    }
}