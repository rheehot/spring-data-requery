package org.springframework.data.requery.repository.query

import io.requery.PersistenceException
import io.requery.query.Result
import mu.KotlinLogging
import org.springframework.core.convert.ConversionService
import org.springframework.core.convert.support.ConfigurableConversionService
import org.springframework.core.convert.support.DefaultConversionService
import org.springframework.data.requery.core.RequeryOperations
import org.springframework.data.requery.getAsResult
import org.springframework.util.ClassUtils

/**
 * RequeryQueryExecution
 *
 * @author debop@coupang.com
 */
abstract class RequeryQueryExecution {

    companion object {

        @JvmStatic private val log = KotlinLogging.logger { }

        @JvmStatic
        private val CONVERSION_SERVICE: ConversionService by lazy {
            val conversionService = DefaultConversionService()

            // Blob to Byte array 로 하는 것은 BlobByteArrayConverter 를 사용하면 된다.
            // conversionService.addConverter(JpaResultConverters.BlobToByteArrayConverter.INSTANCE);

            conversionService.removeConvertible(Collection::class.java, Any::class.java)
            potentiallyRemoveOptionalConverter(conversionService)

            conversionService
        }

        @JvmStatic
        fun potentiallyRemoveOptionalConverter(conversoinService: ConfigurableConversionService) {

            val classLoader = RequeryQueryExecution::class.java.classLoader

            if(ClassUtils.isPresent("java.util.Optional", classLoader)) {
                try {
                    val optionalType = ClassUtils.forName("java.util.Optional", classLoader)
                    conversoinService.removeConvertible(Any::class.java, optionalType)
                } catch(e: ClassNotFoundException) {
                    // Nothing to do.
                } catch(e: LinkageError) {
                    // Nothing to do.
                }
            }
        }
    }

    fun execute(query: AbstractRequeryQuery, values: Array<Any?>): Any? {

        return try {
            doExecute(query, values)
        } catch(pe: PersistenceException) {
            log.error(pe) { "Fail to doExecute. query=$query" }
            return null
        }
    }

    protected abstract fun doExecute(query: AbstractRequeryQuery, values: Array<Any?>): Any?

}


internal class CollectionExecution: RequeryQueryExecution() {

    override fun doExecute(query: AbstractRequeryQuery, values: Array<Any?>): List<*> {
        return query.createQueryElement(values).getAsResult().toList()
    }
}

internal class SlicedExecution(val parameters: RequeryParameters): RequeryQueryExecution() {

    override fun doExecute(query: AbstractRequeryQuery, values: Array<Any?>): Any? {
        TODO("not implemented")
    }
}

internal class PagedExecution(val parameters: RequeryParameters): RequeryQueryExecution() {
    override fun doExecute(query: AbstractRequeryQuery, values: Array<Any?>): Any? {
        TODO("not implemented")
    }
}

internal class SingleEntityExecution: RequeryQueryExecution() {
    override fun doExecute(query: AbstractRequeryQuery, values: Array<Any?>): Any? {
        val result = query.createQueryElement(values).get() as Result<*>
        val value = result.firstOrNull()
        return RequeryResultConverter.convert(value)
    }
}

/**
 *  [RequeryQueryExecution] executing a Java 8 Stream.
 */
internal class StreamExecution(val parameters: RequeryParameters): RequeryQueryExecution() {
    override fun doExecute(query: AbstractRequeryQuery, values: Array<Any?>): Any? {
        TODO("not implemented")
    }
}

internal class DeleteExecution(val operations: RequeryOperations): RequeryQueryExecution() {
    override fun doExecute(query: AbstractRequeryQuery, values: Array<Any?>): Any? {
        TODO("not implemented")
    }
}

internal class ExistsExecution: RequeryQueryExecution() {

    override fun doExecute(query: AbstractRequeryQuery, values: Array<Any?>): Any? {
        return query.createQueryElement(values).limit(1).getAsResult().firstOrNull() != null
    }

}