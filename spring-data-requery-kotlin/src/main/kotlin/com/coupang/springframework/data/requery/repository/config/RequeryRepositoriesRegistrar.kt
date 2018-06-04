package com.coupang.springframework.data.requery.repository.config

import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport
import org.springframework.data.repository.config.RepositoryConfigurationExtension

/**
 * RequeryRepositoriesRegistrar
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
open class RequeryRepositoriesRegistrar: RepositoryBeanDefinitionRegistrarSupport() {

    override fun getAnnotation(): Class<out Annotation> = EnableRequeryRepositories::class.java

    override fun getExtension(): RepositoryConfigurationExtension =
        RequeryRepositoryConfigExtension()


}