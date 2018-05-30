package com.coupang.springframework.data.requery.config

import com.coupang.kotlinx.logging.KLogging
import io.requery.Entity
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.springframework.util.ClassUtils

/**
 * RequeryEntityClassScanner
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
object RequeryEntityClassScanner: KLogging() {

    val ENTITY_ANNOTATIONS = arrayOf(Entity::class.java)

    fun scanForEntities(vararg basePackages: String): Set<Class<*>> {
        return basePackages.flatMap { scanForEntities(it) }.toSet()
    }

    fun scanForEntities(basePackage: String): Set<Class<*>> {

        return if(basePackage.isNotBlank()) {
            val componentProvider = ClassPathScanningCandidateComponentProvider(false)
            ENTITY_ANNOTATIONS.forEach { ann ->
                componentProvider.addIncludeFilter(AnnotationTypeFilter(ann))
            }
            componentProvider.findCandidateComponents(basePackage).mapNotNull { definition ->
                definition.beanClassName?.let {
                    ClassUtils.forName(it, null)
                }
            }.toSet()
        } else {
            emptySet()
        }
    }
}