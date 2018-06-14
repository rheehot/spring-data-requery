package com.coupang.springframework.data.requery.repository.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.data.repository.config.RepositoryBeanDefinitionParser;

/**
 * com.coupang.springframework.data.requery.repository.config.RequeryRepositoryNameSpaceHandler
 *
 * @author debop
 * @since 18. 6. 6
 */
@Slf4j
public class RequeryRepositoryNameSpaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        log.debug("Init and regist BeanDefinitionParser for repositories.");

        RequeryRepositoryConfigurationExtension extension = new RequeryRepositoryConfigurationExtension();
        RepositoryBeanDefinitionParser definitionParser = new RepositoryBeanDefinitionParser(extension);

        registerBeanDefinitionParser("repositories", definitionParser);

        // auditing 은 필요없다.
        // registerBeanDefinitionParser("auditing", new AuditingBeanDefinitionParser());
    }
}
