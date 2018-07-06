@file:JvmName("RequeryExtensions")

package org.springframework.data.requery

import io.requery.meta.Attribute
import io.requery.meta.EntityModel
import io.requery.meta.Type
import io.requery.sql.EntityContext
import io.requery.sql.EntityDataStore
import mu.KotlinLogging
import org.springframework.util.ReflectionUtils
import kotlin.reflect.KClass

private val log = KotlinLogging.logger { }

@Suppress("UNCHECKED_CAST")
fun <E: Any> EntityDataStore<E>.getEntityContext(): EntityContext<E> {
    try {
        val field = ReflectionUtils.findField(this.javaClass, "context")!!
        field.isAccessible = true

        return ReflectionUtils.getField(field, this) as EntityContext<E>
    } catch(e: Exception) {
        throw IllegalArgumentException("Fail to retrieve EntityContext.", e)
    }
}

fun <E: Any> EntityDataStore<E>.getEntityModel(): EntityModel {
    try {
        val field = ReflectionUtils.findField(this.javaClass, "entityModel")!!
        field.isAccessible = true

        return ReflectionUtils.getField(field, this) as EntityModel
    } catch(e: Exception) {
        throw IllegalArgumentException("Fail to retrieve EntityModel", e)
    }
}

fun <E: Any> EntityDataStore<E>.getEntityTypes(): Set<Type<*>> {
    return this.getEntityModel().types
}

fun <E: Any> EntityDataStore<E>.getEntityClasses(): List<Class<*>> {
    return getEntityTypes().map { it.classType }
}

@Suppress("UNCHECKED_CAST")
fun <E: Any> EntityDataStore<*>.getType(entityClass: Class<E>): Type<E>? {
    return getEntityTypes().find { entityClass == it.classType } as? Type<E>
}

@Suppress("UNCHECKED_CAST")
fun <E: Any> EntityDataStore<*>.getType(entityClass: KClass<E>): Type<E>? {
    return getEntityTypes().find { entityClass.java == it.classType } as? Type<E>
}

fun <E: Any> EntityDataStore<*>.getKeyAttributes(entityClass: Class<E>): Set<Attribute<E, *>> {
    return getType(entityClass)?.keyAttributes ?: emptySet()
}

fun <E: Any> EntityDataStore<*>.getSingleKeyAttribute(entityClass: Class<E>): Attribute<E, *> {
    return getType(entityClass)?.singleKeyAttribute!!
}

