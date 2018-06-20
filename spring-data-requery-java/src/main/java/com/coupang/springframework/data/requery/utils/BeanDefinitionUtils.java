package com.coupang.springframework.data.requery.utils;

import io.requery.sql.EntityDataStore;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.util.ClassUtils;

import java.util.*;

import static org.springframework.beans.factory.BeanFactoryUtils.transformedBeanName;

/**
 * BeanDefinitionUtils
 *
 * @author debop@coupang.com
 * @since 18. 6. 14
 * @deprecated Not used
 */
@Deprecated
public class BeanDefinitionUtils {

    private static final String JNDI_OBJECT_FACTORY_BEAN = "org.springframework.jndi.JndiObjectFactoryBean";
    private static final List<Class<?>> EDS_TYPES;

    static {
        List<Class<?>> types = new ArrayList<>();
        types.add(EntityDataStore.class);
        // types.add(EntityDataStoreFactoryBean.class);

        if (ClassUtils.isPresent(JNDI_OBJECT_FACTORY_BEAN, ClassUtils.getDefaultClassLoader())) {
            types.add(JndiObjectFactoryBean.class);
        }

        EDS_TYPES = Collections.unmodifiableList(types);
    }

    public static Iterable<String> getEntityDataStoreFactoryBeanNames(ListableBeanFactory beanFactory) {

        Set<String> names = new HashSet<>();
        names.addAll(Arrays.asList(BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory,
                                                                                       EntityDataStore.class,
                                                                                       true,
                                                                                       false)));

//        for(String factoryBeanName: BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory,
//                                                                                        EntityDataStoreFactoryBean.class,
//                                                                                        true,
//                                                                                        false)) {
//            names.add(BeanFactoryUtils.transformedBeanName(factoryBeanName));
//        }

        return names;
    }

    public static Collection<EntityDataStoreFactoryBeanDefinition> getEntityDataStoreFactoryBeanDefinitions(
        ConfigurableListableBeanFactory beanFactory) {

        List<EntityDataStoreFactoryBeanDefinition> definitions = new ArrayList<>();

        for (Class<?> type : EDS_TYPES) {
            for (String name : beanFactory.getBeanNamesForType(type, true, false)) {
                registerEntityDataStoreFactoryBeanDefinition(transformedBeanName(name), beanFactory, definitions);
            }
        }

        BeanFactory parentBeanFactory = beanFactory.getParentBeanFactory();

        if (parentBeanFactory instanceof ConfigurableListableBeanFactory) {
            definitions.addAll(getEntityDataStoreFactoryBeanDefinitions((ConfigurableListableBeanFactory) parentBeanFactory));
        }

        return definitions;
    }

    private static void registerEntityDataStoreFactoryBeanDefinition(String name,
                                                                     @NotNull ConfigurableListableBeanFactory beanFactory,
                                                                     List<EntityDataStoreFactoryBeanDefinition> definitions) {
        BeanDefinition definition = beanFactory.getBeanDefinition(name);

        if (JNDI_OBJECT_FACTORY_BEAN.equals(definition.getBeanClassName())) {
            if (!EntityDataStore.class.getName().equals(definition.getPropertyValues().get("expectedType"))) {
                return;
            }
        }

        Class<?> type = beanFactory.getType(name);
        if (type == null || !EntityDataStore.class.isAssignableFrom(type)) {
            return;
        }

        definitions.add(new EntityDataStoreFactoryBeanDefinition(name, beanFactory));
    }


    @Getter
    public static class EntityDataStoreFactoryBeanDefinition {

        private final String beanName;
        private final ConfigurableListableBeanFactory beanFactory;

        public EntityDataStoreFactoryBeanDefinition(String beanName, ConfigurableListableBeanFactory beanFactory) {
            this.beanName = beanName;
            this.beanFactory = beanFactory;
        }

        public BeanDefinition getBeanDefinition() {
            return beanFactory.getBeanDefinition(beanName);
        }
    }
}
