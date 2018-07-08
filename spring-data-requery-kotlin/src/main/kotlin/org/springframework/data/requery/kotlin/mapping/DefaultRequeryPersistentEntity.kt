package org.springframework.data.requery.kotlin.mapping

import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.data.mapping.model.BasicPersistentEntity
import org.springframework.data.util.TypeInformation

/**
 * org.springframework.data.requery.mapping.DefaultRequeryPersistentEntity
 *
 * @author debop
 */
class DefaultRequeryPersistentEntity<E>(information: TypeInformation<E>)
    : BasicPersistentEntity<E, RequeryPersistentProperty>(information), RequeryPersistentEntity<E> {

    private val name: String

    override val idProperties = arrayListOf<RequeryPersistentProperty>()
    override val embeddedProperties = arrayListOf<RequeryPersistentProperty>()
    override val indexes = arrayListOf<RequeryPersistentProperty>()

    override val singleIdProperty: RequeryPersistentProperty?
        get() = idProperties.firstOrNull()

    private val annotationCache: MutableMap<Class<out Annotation>, Annotation?> = linkedMapOf()
    private val repeatableAnnotationCache: MutableMap<Class<out Annotation>, Set<Annotation>> = linkedMapOf()

    init {
        this.name = findAnnotation(io.requery.Entity::class.java)?.let {
            when {
                it.name.isNotBlank() -> it.name
                else -> information.type.simpleName
            }
        } ?: information.type.simpleName
    }

    override fun getName(): String {
        return name
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        // Nothing to do.
    }

    override fun addPersistentProperty(property: RequeryPersistentProperty) {
        super.addPersistentProperty(property)

        if(property.isIdProperty) {
            idProperties.add(property)
        }
        if(property.isEmbedded()) {
            embeddedProperties.add(property)
        }
        if(property.hasIndex()) {
            indexes.add(property)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <A: Annotation> findAnnotation(annotationType: Class<A>): A? {
        return annotationCache.computeIfAbsent(annotationType) {
            AnnotatedElementUtils.findMergedAnnotation<A>(type, annotationType)
        } as? A
    }

    @Suppress("UNCHECKED_CAST")
    fun <A: Annotation> findAnnotations(annotationType: Class<A>): Set<A> {
        return repeatableAnnotationCache.computeIfAbsent(annotationType) {
            AnnotatedElementUtils.findMergedRepeatableAnnotations<A>(type, annotationType)
        } as Set<A>
    }
}