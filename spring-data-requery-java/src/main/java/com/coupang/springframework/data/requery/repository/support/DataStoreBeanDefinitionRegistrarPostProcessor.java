package com.coupang.springframework.data.requery.repository.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * DataStoreBeanDefinitionRegistrarPostProcessor
 *
 * @author debop@coupang.com
 * @since 18. 6. 14
 */
public class DataStoreBeanDefinitionRegistrarPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
