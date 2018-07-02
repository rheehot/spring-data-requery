package org.springframework.data.requery.repository.query

import io.requery.query.Tuple
import mu.KotlinLogging


object RequeryResultConverter {

    private val log = KotlinLogging.logger { }

    fun convert(result: Any?, defaultValue: Any? = null): Any? {

        log.trace { "Convert result. result=$result" }

        // TODO: 원하는 수형으로 변환하도록 해야 한다.
        return try {
            when(result) {
                is Tuple ->
                    when {
                        result.count() == 1 -> result.get(0)
                        else -> result
                    }
                else -> result
            }
        } catch(ignored: Exception) {
            log.warn(ignored) { "Fail to convert result[$result], return [$defaultValue]" }
            defaultValue
        }
    }
}