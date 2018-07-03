package org.springframework.data.requery.repository.query

import mu.KotlinLogging
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.data.projection.ProjectionFactory
import org.springframework.data.repository.core.RepositoryMetadata
import org.springframework.data.repository.query.Parameters
import org.springframework.data.repository.query.QueryMethod
import org.springframework.data.requery.Annotations
import org.springframework.data.requery.annotation.Query
import java.lang.reflect.Method

/**
 * RequeryQueryMethod
 *
 * @author debop@coupang.com
 */
class RequeryQueryMethod(val method: Method,
                         val metadata: RepositoryMetadata,
                         val factory: ProjectionFactory): QueryMethod(method, metadata, factory) {

    companion object {
        private val log = KotlinLogging.logger { }

        // TODO: 혹시 이 것이 안되면, Java code에서 import 해야 한다.
        private val NATIVE_ARRAY_TYPES: Set<Class<*>> by lazy {
            LinkedHashSet<Class<*>>().apply {
                add(ByteArray::class.java)
                add(CharArray::class.java)
            }
        }
    }

    val entityInformation: RequeryEntityMetadata<*> = DefaultRequeryEntityMetadata.of(domainClass)

    init {
        log.debug { "Create RequeryQueryMethod. repository=${metadata.repositoryInterface}, method name=${method.name}, method=$method" }

        check(!isModifyingQuery || !parameters.hasSpecialParameter()) {
            "Modifying query method must not contains ${Parameters.TYPES}"
        }

        assertParameterNamesInAnnotatedQuery()
    }

    private fun assertParameterNamesInAnnotatedQuery() {

        val query = annotatedQuery

        parameters
            .filter { it.isNamedParameter }
            .forEach {

                val paramName = it.name.orElse("")

                // Named Parameter 인데 Query에 ':paramName', '#paramName' 구문이 없다면 예외를 발생시킨다.
                if(query.isNullOrBlank() ||
                   (query != null && paramName.isNotBlank() && !query.contains(":" + paramName) && !query.contains("#" + paramName))) {
                    error("Using named parameters for query method [$method] but parameter '${it.name}' not found in annotated query '$query'!")
                }
            }
    }

    val isAnnotatedQuery: Boolean
        get() = Annotations.findAnnotation(method, Query::class.java) != null

    val isDefaultMethod: Boolean
        get() = method.isDefault

    val isOverridedMethod: Boolean
        get() = method.declaringClass != metadata.repositoryInterface

    internal val returnType: Class<*>
        get() = method.returnType

    val annotatedQuery: String?
        get() {
            val query = getAnnotationValue("value", String::class.java)
            return when {
                query.isNullOrBlank() -> null
                else -> query
            }
        }


    private fun getAnnotationValue(attribute: String, type: Class<String>): String? {
        return getMergedOrDefaultAnnotationValue(attribute, Query::class.java, type)
    }

    private fun <T> getMergedOrDefaultAnnotationValue(attribute: String,
                                                      annotationType: Class<out Annotation>,
                                                      targetType: Class<T>): T? {
        val annotation = AnnotatedElementUtils.findMergedAnnotation(method, annotationType)

        return annotation?.let {
            targetType.cast(AnnotationUtils.getValue(annotation, attribute))
        } ?: targetType.cast(AnnotationUtils.getDefaultValue(annotationType, attribute))
    }

    override fun createParameters(method: Method): Parameters<*, *> = RequeryParameters(method)

    override fun getParameters(): RequeryParameters = super.getParameters() as RequeryParameters

    override fun isCollectionQuery(): Boolean =
        super.isCollectionQuery() && !NATIVE_ARRAY_TYPES.contains(method.returnType)

}