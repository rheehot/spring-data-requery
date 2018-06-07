package com.coupang.springframework.data.requery.repository.config;

import com.coupang.springframework.data.requery.repository.RequeryRepository;
import com.coupang.springframework.data.requery.repository.support.RequeryRepositoryFactoryBean;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.repository.config.AnnotationRepositoryConfigurationSource;
import org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport;
import org.springframework.data.repository.config.RepositoryConfigurationSource;
import org.springframework.data.repository.config.XmlRepositoryConfigurationSource;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * com.coupang.springframework.data.requery.repository.config.RequeryRepositoryConfigExtension
 *
 * @author debop
 * @since 18. 6. 6
 */
public class RequeryRepositoryConfigExtension extends RepositoryConfigurationExtensionSupport {

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
        return Arrays.asList(io.requery.Entity.class, io.requery.Superclass.class);
    }

    @NotNull
    @Override
    protected Collection<Class<?>> getIdentifyingTypes() {
        return Collections.singleton(RequeryRepository.class);
    }


    @Override
    public void postProcess(BeanDefinitionBuilder builder, RepositoryConfigurationSource source) {
        Optional<String> transactionManagerRef = source.getAttribute("transactionManagerRef");

        throw new NotImplementedException("not implemented");
    }

    @Override
    public void postProcess(BeanDefinitionBuilder builder, AnnotationRepositoryConfigurationSource config) {
        throw new NotImplementedException("not implemented");
    }

    @Override
    public void postProcess(BeanDefinitionBuilder builder, XmlRepositoryConfigurationSource config) {
        throw new NotImplementedException("not implemented");
    }

    @Override
    public void registerBeansForRoot(BeanDefinitionRegistry registry, RepositoryConfigurationSource configurationSource) {
        throw new NotImplementedException("not implemented");
    }

    @Override
    protected ClassLoader getConfigurationInspectionClassLoader(ResourceLoader loader) {
        ClassLoader classLoader = loader.getClassLoader();

        return classLoader != null && LazyJvmAgent.isActive(classLoader)
               ? new InspectionClassLoader(classLoader)
               : classLoader;
    }

    private static AbstractBeanDefinition getEntityManagerBeanDefinitionFor(RepositoryConfigurationSource config,
                                                                            @Nullable Object source) {
        throw new NotImplementedException("not implemented");
    }

    private static String getEntityDataSourceBeanRef(@NotNull RepositoryConfigurationSource config) {
        return config.getAttribute("entityDataSourceRef")
            .orElse("entityDataSource");
    }

    @UtilityClass
    static class LazyJvmAgent {

        private static final Set<String> AGENT_CLASSES;

        static {
            Set<String> agentClasses = new LinkedHashSet<>();

            agentClasses.add("org.springframework.instrument.InstrumentationSavingAgent");
            //agentClasses.add("org.eclipse.persistence.internal.jpa.deployment.JavaSECMPInitializerAgent");

            AGENT_CLASSES = Collections.unmodifiableSet(agentClasses);
        }

        static boolean isActive(@Nullable ClassLoader classLoader) {
            return AGENT_CLASSES.stream()
                .anyMatch(agentClass -> ClassUtils.isPresent(agentClass, classLoader));
        }

    }
}
