package org.springframework.data.requery.mapping

import org.springframework.data.mapping.Association
import org.springframework.data.mapping.PersistentEntity
import org.springframework.data.mapping.model.*
import org.springframework.util.StringUtils

/**
 * Default implementation of [RequeryPersistentProperty]
 *
 * @author debop
 */
class DefaultRequeryPersistentProperty @JvmOverloads constructor(
    property: Property,
    owner: PersistentEntity<*, RequeryPersistentProperty>,
    simpleTypeHolder: SimpleTypeHolder,
    val fieldNamingStrategy: FieldNamingStrategy = PropertyNameFieldNamingStrategy.INSTANCE
): AnnotationBasedPersistentProperty<RequeryPersistentProperty>(property, owner, simpleTypeHolder), RequeryPersistentProperty {

    override fun createAssociation(): Association<RequeryPersistentProperty> =
        Association(this, null)

    override fun isIdProperty(): Boolean = findAnnotation(io.requery.Key::class.java) != null

    override fun isAssociation(): Boolean =
        findAnnotation(io.requery.OneToOne::class.java) != null ||
        findAnnotation(io.requery.OneToMany::class.java) != null ||
        findAnnotation(io.requery.ManyToOne::class.java) != null ||
        findAnnotation(io.requery.ManyToMany::class.java) != null

    override fun isEmbedded(): Boolean = findAnnotation(io.requery.Embedded::class.java) != null

    override fun hasIndex(): Boolean = findAnnotation(io.requery.Index::class.java) != null

    override fun isTransient(): Boolean = findAnnotation(io.requery.Transient::class.java) != null

    override fun isVersionProperty(): Boolean = findAnnotation(io.requery.Version::class.java) != null

    override fun getFieldName(): String {
        return when {
            isIdProperty -> property.name
            else         -> getAnnotatedColumnName() ?: fieldNamingStrategy.getFieldName(this)
        }
    }

    private fun getAnnotatedColumnName(): String? {
        return findAnnotation(io.requery.Column::class.java)?.let {
            when {
                StringUtils.hasText(it.value) -> it.value
                else                          -> null
            }
        }
    }
}