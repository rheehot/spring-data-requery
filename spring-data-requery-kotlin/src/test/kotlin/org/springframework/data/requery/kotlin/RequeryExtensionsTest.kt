package org.springframework.data.requery.kotlin

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.data.requery.kotlin.domain.AbstractDomainTest
import org.springframework.data.requery.kotlin.domain.sample.User

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

        assertThat(requeryTemplate.entityContext).isEqualTo(entityContext)
    }

    @Test
    fun `retrieve EntityModel`() {
        val entityModel = dataStore.getEntityModel()
        assertThat(entityModel).isNotNull
        assertThat(entityModel.name).isEqualTo("default")

        assertThat(requeryTemplate.entityModel).isEqualTo(entityModel)
    }

    @Test
    fun `retrieve entity types from EntityDataStore`() {
        val types = dataStore.getEntityTypes()
        assertThat(types).isNotEmpty.contains(User.`$TYPE`)
    }

    @Test
    fun `retrieve entity classes from EntityDataStore`() {
        val classes = dataStore.getEntityClasses()
        assertThat(classes).isNotEmpty.contains(User::class.java)
    }

    @Test
    fun `retrieve type from EntityDataStore`() {
        assertThat(dataStore.getType(User::class.java)).isEqualTo(User.`$TYPE`)
        assertThat(dataStore.getType(User::class)).isEqualTo(User.`$TYPE`)
    }

    @Test
    fun `get Identifier attribute from entity`() {

        val keyAttrs = dataStore.getKeyAttributes(User::class.java)
        assertThat(keyAttrs).hasSize(1)
        assertThat(keyAttrs.first().classType).isEqualTo(Integer::class.java)
    }

    @Test
    fun `get single id attribute from domain class`() {

        val keyAttr = dataStore.getSingleKeyAttribute(User::class.java)
        assertThat(keyAttr).isNotNull
        assertThat(keyAttr.classType).isEqualTo(Integer::class.java)
    }
}