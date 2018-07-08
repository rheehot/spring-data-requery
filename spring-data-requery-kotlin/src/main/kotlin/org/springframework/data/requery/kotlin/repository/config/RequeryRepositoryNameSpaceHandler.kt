package org.springframework.data.requery.repository.config

import mu.KotlinLogging.logger
import org.springframework.beans.factory.xml.NamespaceHandlerSupport
import org.springframework.data.repository.config.RepositoryBeanDefinitionParser

/**
 * org.springframework.data.requery.repository.config.RequeryRepositoryNameSpaceHandler
 *
 * @author debop
 */
class RequeryRepositoryNameSpaceHandler: NamespaceHandlerSupport() {

    private val log = logger { }

    override fun init() {
        log.debug { "Init and regist BeanDefinitionParser for repositories." }

        val extension = RequeryRepositoryConfigurationExtension()
        val definitionParser = RepositoryBeanDefinitionParser(extension)

        registerBeanDefinitionParser("repositories", definitionParser)

        // auditing 은 필요없다.
        // registerBeanDefinitionParser("auditing", new AuditingBeanDefinitionParser());
    }
}