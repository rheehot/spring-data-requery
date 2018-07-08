package org.springframework.data.requery.kotlin.repository.query

import io.requery.Entity
import mu.KotlinLogging
import org.springframework.core.annotation.AnnotatedElementUtils
import kotlin.reflect.KClass

/**
 * Default implementation for [RequeryEntityMetadata].
 *
 * @author debop@coupang.com
 */
class DefaultRequeryEntityMetadata<E: Any>(override val kotlinType: KClass<E>): RequeryEntityMetadata<E> {

    companion object {
        private val log = KotlinLogging.logger { }

        private const val DEFAULT_MODEL_NAME = "default"

        @JvmStatic
        fun <E: Any> of(domainKlass: KClass<E>): DefaultRequeryEntityMetadata<E> =
            DefaultRequeryEntityMetadata(domainKlass)
    }

    private val entity: Entity? by lazy {
        AnnotatedElementUtils.findMergedAnnotation(kotlinType.java, io.requery.Entity::class.java)
    }

    override val entityName: String by lazy {
        log.trace { "Get entity name ... domainClass=$kotlinType, entity=$entity" }

        val entity = this.entity
        when {
            entity != null && entity.name.isNotBlank() -> entity.name
            else -> getEntityName(kotlinType.java.simpleName)
        }
    }

    private fun getEntityName(simpleName: String): String {
        return when {
            simpleName.startsWith("Abstract") -> simpleName.removePrefix("Abstract")
            simpleName.endsWith("Entity") -> simpleName.removeSuffix("Entity")
            else -> simpleName
        }
    }


    override val modelName: String by lazy {
        log.trace { "Get model name ... domainClass=$kotlinType, entity=$entity" }

        entity?.let {
            if(it.model.isNotBlank()) it.model else DEFAULT_MODEL_NAME
        } ?: ""
    }

    override fun getJavaType(): Class<E> = kotlinType.java

}