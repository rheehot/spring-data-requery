package com.coupang.springframework.data.requery.repository.config;

import com.coupang.springframework.data.requery.repository.RequeryRepository;
import com.coupang.springframework.data.requery.repository.support.RequeryRepositoryFactoryBean;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.repository.config.AnnotationRepositoryConfigurationSource;
import org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport;
import org.springframework.data.repository.config.RepositoryConfigurationSource;
import org.springframework.data.repository.config.XmlRepositoryConfigurationSource;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * com.coupang.springframework.data.requery.repository.config.RequeryRepositoryConfigurationExtension
 *
 * @author debop
 * @since 18. 6. 6
 */
@Slf4j
public class RequeryRepositoryConfigurationExtension extends RepositoryConfigurationExtensionSupport {

    private static final String DEFAULT_TRANSACTION_MANAGER_BEAN_NAME = "transactionManager";
    private static final String ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE = "enableDefaultTransactions";

    @NotNull
    @Override
    public String getModuleName() {
        return "REQUERY";
    }

    @NotNull
    @Override
    public String getRepositoryFactoryBeanClassName() {
        return RequeryRepositoryFactoryBean.class.getName();
    }

    @NotNull
    @Override
    protected String getModulePrefix() {
        return getModuleName().toLowerCase(Locale.US);
    }

    @NotNull
    @Override
    protected Collection<Class<? extends Annotation>> getIdentifyingAnnotations() {
        return Arrays.asList(io.requery.Entity.class,
                             io.requery.Superclass.class);
    }

    @NotNull
    @Override
    protected Collection<Class<?>> getIdentifyingTypes() {
        return Collections.singleton(RequeryRepository.class);
    }

    @Override
    public void postProcess(BeanDefinitionBuilder builder, RepositoryConfigurationSource source) {

        Optional<String> transactionManagerRef = source.getAttribute("transactionManagerRef");

        builder.addPropertyValue(DEFAULT_TRANSACTION_MANAGER_BEAN_NAME, transactionManagerRef.orElse(DEFAULT_TRANSACTION_MANAGER_BEAN_NAME));
    }

    @Override
    public void postProcess(BeanDefinitionBuilder builder, AnnotationRepositoryConfigurationSource config) {

        AnnotationAttributes attributes = config.getAttributes();

        builder.addPropertyValue(ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE,
                                 attributes.getBoolean(ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE));
    }

    @Override
    public void postProcess(BeanDefinitionBuilder builder, XmlRepositoryConfigurationSource config) {

        Optional<String> enableDefaultTransactions = config.getAttribute(ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE);

        if (enableDefaultTransactions.isPresent() && StringUtils.hasText(enableDefaultTransactions.get())) {
            builder.addPropertyValue(ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE, enableDefaultTransactions.get());
        }
    }

    @Nullable
    @Override
    protected ClassLoader getConfigurationInspectionClassLoader(ResourceLoader loader) {

        ClassLoader classLoader = loader.getClassLoader();

        return classLoader != null && LazyJvmAgent.isActive(classLoader)
               ? new InspectionClassLoader(classLoader)
               : classLoader;
    }


    @UtilityClass
    static class LazyJvmAgent {

        private static final Set<String> AGENT_CLASSES;

        static {
            Set<String> agentClasses = new LinkedHashSet<>();

            agentClasses.add("org.springframework.instrument.InstrumentationSavingAgent");
            agentClasses.add("org.eclipse.persistence.internal.jpa.deployment.JavaSECMPInitializerAgent");

            AGENT_CLASSES = Collections.unmodifiableSet(agentClasses);
        }

        static boolean isActive(@Nullable ClassLoader classLoader) {
            return AGENT_CLASSES.stream()
                .anyMatch(it -> ClassUtils.isPresent(it, classLoader));
        }
    }
}
