package org.springframework.data.requery.kotlin.repository.config

import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport
import org.springframework.data.repository.config.RepositoryConfigurationExtension

/**
 * [ImportBeanDefinitionRegistrar] to enable [EnableRequeryRepositories] annotation.
 *
 * @author debop
 */
class RequeryRepositoriesRegistar: RepositoryBeanDefinitionRegistrarSupport() {

    override fun getAnnotation(): Class<out Annotation> =
        EnableRequeryRepositories::class.java

    override fun getExtension(): RepositoryConfigurationExtension =
        RequeryRepositoryConfigurationExtension()


}