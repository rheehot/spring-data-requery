@file:JvmName("ClassExtensions")

package org.springframework.data.requery

import io.requery.query.NamedExpression
import mu.KotlinLogging
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.util.LinkedMultiValueMap
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.concurrent.ConcurrentHashMap

private val log = KotlinLogging.logger {}

private val classFieldCache = ConcurrentHashMap<String, Field?>()
private val classMethodCache = ConcurrentHashMap<String, Method?>()

private val entityFields = LinkedMultiValueMap<Class<*>, Field>()
private val entityMethods = LinkedMultiValueMap<Class<*>, Method>()


fun Class<*>.findField(fieldName: String): Field? {

    val cacheKey = "$name.$fieldName"

    return classFieldCache.computeIfAbsent(cacheKey) { _ ->

        var targetClass: Class<*>? = this@findField

        while(targetClass != null && targetClass.isRequeryEntity) {
            try {
                val foundField = targetClass?.getDeclaredField(fieldName)
                if(foundField != null)
                    return@computeIfAbsent foundField
            } catch(e: Exception) {
                // Nothing to do.
            }
            targetClass = targetClass?.superclass
        }
        null
    }
}

fun Class<*>.findFields(predicate: (Field) -> Boolean): List<Field> {

    val foundFields = mutableListOf<Field>()
    var targetClass: Class<*>? = this

    while(targetClass != null && targetClass.isRequeryEntity) {
        val fields = targetClass?.declaredFields?.filter { predicate(it) }?.toList()
        fields?.let { foundFields.addAll(it) }

        targetClass = targetClass?.superclass
    }

    return foundFields
}

fun Class<*>.findFirstField(predicate: (Field) -> Boolean): Field? {

    var targetClass: Class<*>? = this

    while(targetClass != null && targetClass.isRequeryEntity) {
        log.trace { "Find first field... targetClass=${targetClass?.name}" }

        val field = targetClass?.declaredFields?.find {
            predicate(it)
        }

        log.trace { "found field=$field" }
        if(field != null)
            return field

        targetClass = targetClass.superclass
    }

    return null
}

fun Class<*>.findMethod(methodName: String, vararg paramTypes: Class<*>): Method? {
    val cacheKey = "$name.$methodName.${paramTypes.joinToString()}"

    return classMethodCache.computeIfAbsent(cacheKey) {
        var targetClass: Class<*>? = this@findMethod

        while(targetClass != null && targetClass.isRequeryEntity) {
            val foundMethod = targetClass?.getDeclaredMethod(name, *paramTypes)
            if(foundMethod != null)
                return@computeIfAbsent foundMethod

            targetClass = targetClass?.superclass
        }
        null
    }
}

fun Class<*>.findMethods(predicate: (Method) -> Boolean): List<Method> {

    val foundMethods = mutableListOf<Method>()
    var targetClass: Class<*>? = this

    while(targetClass != null && targetClass.isRequeryEntity) {
        val methods = targetClass.declaredMethods?.filter { predicate(it) }?.toList()
        methods?.let { foundMethods.addAll(it) }

        targetClass = targetClass.superclass
    }

    return foundMethods
}


fun Class<*>.findFirstMethod(predicate: (Method) -> Boolean): Method? {

    var targetClass: Class<*>? = this

    while(targetClass != null && targetClass.isRequeryEntity) {
        log.trace { "Find first method... targetClass=${targetClass?.name}" }

        val method = targetClass?.declaredMethods?.find {
            predicate(it)
        }

        log.trace { "found method=$method" }
        if(method != null)
            return method

        targetClass = targetClass.superclass
    }

    return null
}

val Class<*>.isRequeryEntity: Boolean
    get() = AnnotationUtils.findAnnotation(this, io.requery.Entity::class.java) != null

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


private val classKeyExpressions = ConcurrentHashMap<Class<*>, NamedExpression<*>>()
var UNKNOWN_KEY_EXPRESSION: NamedExpression<*> = NamedExpression.of("Unknown", Any::class.java)

@Suppress("UNCHECKED_CAST")
fun <V: Any> Class<*>.getKeyExpression(): NamedExpression<V> {

    return classKeyExpressions.computeIfAbsent(this) { domainClass ->

        // NOTE: Java entity 는 Field로 등록된 id 값을 반환한다.
        // NOTE: Kotlin의 경우는 getId() 메소드로부터 반환한다.
        val field = domainClass.findFirstField { it.getAnnotation(io.requery.Key::class.java) != null }

        when(field) {
            null -> {
                val method = domainClass.findFirstMethod { it.getAnnotation(io.requery.Key::class.java) != null }
                when(method) {
                    null -> {
                        log.debug { "Not found @Key property. class=${this.simpleName} " }
                        UNKNOWN_KEY_EXPRESSION
                    }
                    else -> namedExpressionOf(method.extractFieldname(), method.returnType)
                }
            }
            else -> namedExpressionOf(field.name, field.type)
        }
    } as NamedExpression<V>
}

/**
 * Getter method로부터 field name을 추출합니다.
 */
fun Method.extractFieldname(): String {
    return when {
        name.contains("get") -> name.removePrefix("get").decapitalize()
        name.contains("set") -> name.removePrefix("set").decapitalize()
        else -> name.decapitalize()
    }
}
