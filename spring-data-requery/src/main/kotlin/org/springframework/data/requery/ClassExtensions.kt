package org.springframework.data.requery

import io.requery.query.NamedExpression
import mu.KLogging
import org.springframework.util.LinkedMultiValueMap
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.concurrent.ConcurrentHashMap

object CEX: KLogging()


private val classFieldCache = ConcurrentHashMap<String, Field?>()
private val entityFields = LinkedMultiValueMap<Class<*>, Field>()

fun Class<*>.findField(fieldName: String): Field? {

    val cacheKey = "$name.$fieldName"

    return classFieldCache.computeIfAbsent(cacheKey) { _ ->

        var targetClass: Class<*>? = this@findField

        do {
            try {
                val foundField = targetClass?.getDeclaredField(fieldName)
                if(foundField != null)
                    return@computeIfAbsent foundField
            } catch(e: Exception) {
                // Nothing to do.
            }
            targetClass = targetClass?.superclass
        } while(targetClass != null && targetClass != Any::class.java)

        null
    }
}

fun Class<*>.findFields(predicate: (Field) -> Boolean): List<Field> {

    val foundFields = mutableListOf<Field>()
    var targetClass: Class<*>? = this

    do {
        targetClass
            ?.declaredFields
            ?.filter { predicate(it) }
            ?.forEach {
                foundFields.add(it)
            }
        targetClass = targetClass?.superclass
    } while(targetClass != null && targetClass != Any::class.java)

    return foundFields
}

fun Class<*>.findFirstField(predicate: (Field) -> Boolean): Field? {

    var targetClass: Class<*>? = this

    do {
        val field = targetClass?.declaredFields?.find { predicate(it) }

        if(field != null)
            return field

        targetClass = targetClass?.superclass
    } while(targetClass != null && targetClass != Any::class.java)

    return null
}

fun Class<*>.findEntityFields(): List<Field> {

    return entityFields.computeIfAbsent(this) { clazz ->
        clazz.findFields(Field::isRequeryEntityField)
    }
}

fun Field.isRequeryEntityField(): Boolean {
    return !isRequeryGeneratedField()
}

fun Field.isRequeryGeneratedField(): Boolean {

    return (modifiers and Modifier.STATIC) > 0 ||
           (name == "\$proxy") ||
           (name.startsWith("\$") && name.endsWith("_state"))
}

fun Field.isKeyField(): Boolean = isAnnotationPresent(io.requery.Key::class.java)

fun Field.isTransientField(): Boolean = isAnnotationPresent(io.requery.Transient::class.java)

fun Field.isEmbeddedField(): Boolean = isAnnotationPresent(io.requery.Embedded::class.java)

fun Field.isAssociationField(): Boolean =
    isAnnotationPresent(io.requery.OneToOne::class.java) ||
    isAnnotationPresent(io.requery.OneToMany::class.java) ||
    isAnnotationPresent(io.requery.ManyToOne::class.java) ||
    isAnnotationPresent(io.requery.ManyToMany::class.java)


private val classKeys = ConcurrentHashMap<Class<*>, NamedExpression<*>>()
var UNKNOWN_KEY_EXPRESSION: NamedExpression<*> = NamedExpression.of("Unknown", Any::class.java)

@Suppress("UNCHECKED_CAST")
fun <V: Any> Class<*>.getKeyExpression(): NamedExpression<V> {

    return classKeys.computeIfAbsent(this) { domainClass ->
        val field = domainClass.findFirstField { it.getAnnotation(io.requery.Key::class.java) != null }

        field?.let { namedExpresesionOf(field.name, field.type) }
        ?: UNKNOWN_KEY_EXPRESSION
    } as NamedExpression<V>
}