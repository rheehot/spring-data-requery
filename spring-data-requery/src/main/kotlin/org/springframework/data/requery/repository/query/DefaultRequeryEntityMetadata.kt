package org.springframework.data.requery.repository.query

import io.requery.Entity
import mu.KotlinLogging
import org.springframework.core.annotation.AnnotatedElementUtils

/**
 * DefaultRequeryEntityMetadata
 *
 * @author debop@coupang.com
 */
class DefaultRequeryEntityMetadata<E>(val domainClass: Class<E>): RequeryEntityMetadata<E> {

    companion object {
        private val log = KotlinLogging.logger { }

        private const val DEFAULT_MODEL_NAME = "default"

        @JvmStatic
        fun <E> of(domainClass: Class<E>): DefaultRequeryEntityMetadata<E> =
            DefaultRequeryEntityMetadata(domainClass)
    }

    private val entity: Entity? by lazy {
        AnnotatedElementUtils.findMergedAnnotation(domainClass, io.requery.Entity::class.java)
    }

    override val entityName: String by lazy {
        log.trace { "Get entity name ... domainClass=$domainClass, entity=$entity" }

        val entity = this.entity
        when {
            entity != null && entity.name.isNotBlank() -> entity.name
            else -> domainClass.simpleName
        }
    }


    override val modelName: String by lazy {
        log.trace { "Get model name ... domainClass=$domainClass, entity=$entity" }

        entity?.let {
            if(it.model.isNotBlank()) it.model else DEFAULT_MODEL_NAME
        } ?: ""
    }

    override fun getJavaType(): Class<E> = domainClass

}