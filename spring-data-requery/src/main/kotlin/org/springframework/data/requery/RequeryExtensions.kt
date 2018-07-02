package org.springframework.data.requery

import io.requery.meta.Attribute
import io.requery.meta.EntityModel
import io.requery.meta.Type
import io.requery.sql.EntityContext
import io.requery.sql.EntityDataStore
import org.springframework.util.ReflectionUtils


@Suppress("UNCHECKED_CAST")
fun <E> EntityDataStore<E>.getEntityContext(): EntityContext<E> {
    try {
        val field = ReflectionUtils.findField(this.javaClass, "context")!!
        field.isAccessible = true

        return ReflectionUtils.getField(field, this) as EntityContext<E>
    } catch(e: Exception) {
        throw IllegalArgumentException("Fail to retrieve EntityContext.", e)
    }
}

fun <T> EntityDataStore<T>.getEntityModel(): EntityModel {
    try {
        val field = ReflectionUtils.findField(this.javaClass, "entityModel")!!
        field.isAccessible = true

        return ReflectionUtils.getField(field, this) as EntityModel
    } catch(e: Exception) {
        throw IllegalArgumentException("Fail to retrieve EntityModel", e)
    }
}

fun <T> EntityDataStore<T>.getEntityTypes(): Set<Type<*>> {
    return this.getEntityModel().types
}

fun <T> EntityDataStore<T>.getEntityClasses(): List<Class<*>> {
    return getEntityTypes().map { it.classType }
}

@Suppress("UNCHECKED_CAST")
fun <E> EntityDataStore<*>.getType(entityClass: Class<E>): Type<E>? {
    return getEntityTypes()
        .find { it -> entityClass.equals(it.classType) } as? Type<E>
}

fun <E> EntityDataStore<*>.getKeyAttributes(entityClass: Class<E>): Set<Attribute<E, *>> {
    return getType(entityClass)?.keyAttributes ?: emptySet()
}

fun <E> EntityDataStore<*>.getSingleKeyAttribute(entityClass: Class<E>): Attribute<E, *> {
    return getType(entityClass)?.singleKeyAttribute!!
}

