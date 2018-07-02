package org.springframework.data.requery.repository.config

import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.core.io.ResourceLoader
import org.springframework.data.repository.config.AnnotationRepositoryConfigurationSource
import org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport
import org.springframework.data.repository.config.RepositoryConfigurationSource
import org.springframework.data.repository.config.XmlRepositoryConfigurationSource
import org.springframework.data.requery.repository.RequeryRepository
import org.springframework.data.requery.repository.support.RequeryRepositoryFactoryBean
import org.springframework.util.ClassUtils
import java.util.*

/**
 * org.springframework.data.requery.repository.config.RequeryRepositoryConfigurationExtension
 *
 * @author debop
 */
class RequeryRepositoryConfigurationExtension: RepositoryConfigurationExtensionSupport() {

    companion object {
        private const val DEFAULT_TRANSACTION_MANAGER_BEAN_NAME = "transactionManager"
        private const val ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE = "enableDefaultTransactions"
    }

    override fun getModuleName(): String = "REQUERY"

    override fun getRepositoryFactoryBeanClassName(): String =
        RequeryRepositoryFactoryBean::class.java.name

    override fun getModulePrefix(): String = moduleName.toLowerCase(Locale.US)

    override fun getIdentifyingAnnotations(): MutableCollection<Class<out Annotation>> =
        arrayListOf(io.requery.Entity::class.java,
                    io.requery.Superclass::class.java)

    override fun getIdentifyingTypes(): MutableCollection<Class<*>> =
        arrayListOf(RequeryRepository::class.java)


    override fun postProcess(builder: BeanDefinitionBuilder, source: RepositoryConfigurationSource) {

        val transactionManagerRef = source.getAttribute("transactionManagerRef")
        builder.addPropertyValue(DEFAULT_TRANSACTION_MANAGER_BEAN_NAME,
                                 transactionManagerRef.orElse(DEFAULT_TRANSACTION_MANAGER_BEAN_NAME))
    }

    override fun postProcess(builder: BeanDefinitionBuilder, config: AnnotationRepositoryConfigurationSource) {

        builder.addPropertyValue(ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE,
                                 config.attributes.getBoolean(ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE))
    }

    override fun postProcess(builder: BeanDefinitionBuilder, config: XmlRepositoryConfigurationSource) {
        val enableDefaultTransactions = config.getAttribute(ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE)

        if(enableDefaultTransactions.isPresent && enableDefaultTransactions.get().isNotBlank()) {
            builder.addPropertyValue(ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE,
                                     enableDefaultTransactions.get())
        }
    }

    override fun getConfigurationInspectionClassLoader(loader: ResourceLoader): ClassLoader? {

        return if(loader.classLoader != null && LazyJvmAgent.isActive(loader.classLoader))
            InspectionClassLoader(loader.classLoader!!)
        else loader.classLoader
    }


    object LazyJvmAgent {

        private val AGENT_CLASSES: Set<String> by lazy {
            LinkedHashSet<String>().apply {
                add("org.springframework.instrument.InstrumentationSavingAgent")
                // add("org.eclipse.persistence.internal.jpa.deployment.JavaSECMPInitializerAgent")
            }
        }

        fun isActive(classLoader: ClassLoader?): Boolean =
            AGENT_CLASSES.any { ClassUtils.isPresent(it, classLoader) }
    }
}