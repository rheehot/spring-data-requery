package com.coupang.springframework.data.requery.repository.query

import org.springframework.data.projection.ProjectionFactory
import org.springframework.data.repository.core.RepositoryMetadata
import org.springframework.data.repository.query.QueryMethod
import org.springframework.data.util.ClassTypeInformation
import java.lang.reflect.Method

/**
 * RequeryQueryMethod
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
open class RequeryQueryMethod(private val method: Method,
                              metadata: RepositoryMetadata,
                              factory: ProjectionFactory): QueryMethod(method, metadata, factory) {

    private val returnType = ClassTypeInformation.from(metadata.repositoryInterface).getReturnType(method)

    override fun getParameters(): RequeryParameters = super.getParameters() as RequeryParameters
}