package org.springframework.data.requery.mapping

import mu.KotlinLogging
import org.springframework.data.mapping.Association
import org.springframework.data.mapping.PersistentEntity
import org.springframework.data.mapping.model.*

/**
 * Default implementation of [RequeryPersistentProperty]
 *
 * @author debop
 */
open class DefaultRequeryPersistentProperty @JvmOverloads constructor(
    property: Property,
    owner: PersistentEntity<*, RequeryPersistentProperty>,
    simpleTypeHolder: SimpleTypeHolder,
    val fieldNamingStrategy: FieldNamingStrategy = PropertyNameFieldNamingStrategy.INSTANCE
): AnnotationBasedPersistentProperty<RequeryPersistentProperty>(property, owner, simpleTypeHolder),
   RequeryPersistentProperty {

    companion object {
        private val log = KotlinLogging.logger { }
    }

    open override fun createAssociation(): Association<RequeryPersistentProperty> = Association(this, null)

    open override fun isIdProperty(): Boolean = findAnnotation(io.requery.Key::class.java) != null

    open override fun isAssociation(): Boolean =
        findAnnotation(io.requery.OneToOne::class.java) != null ||
        findAnnotation(io.requery.OneToMany::class.java) != null ||
        findAnnotation(io.requery.ManyToOne::class.java) != null ||
        findAnnotation(io.requery.ManyToMany::class.java) != null

    open override fun isEmbedded(): Boolean = findAnnotation(io.requery.Embedded::class.java) != null

    open override fun hasIndex(): Boolean = findAnnotation(io.requery.Index::class.java) != null

    open override fun isTransient(): Boolean = findAnnotation(io.requery.Transient::class.java) != null

    open override fun isVersionProperty(): Boolean = findAnnotation(io.requery.Version::class.java) != null

    open override fun getFieldName(): String {
        return when {
            isIdProperty -> property.name
            else -> getAnnotatedColumnName() ?: fieldNamingStrategy.getFieldName(this)
        }
    }


    private fun getAnnotatedColumnName(): String? {

        return findAnnotation(io.requery.Column::class.java)?.let {
            when {
                it.value.isBlank() -> null
                else -> it.value
            }
        }
    }
}