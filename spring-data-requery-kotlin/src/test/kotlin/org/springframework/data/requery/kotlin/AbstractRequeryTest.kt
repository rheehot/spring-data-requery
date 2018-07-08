package org.springframework.data.requery.kotlin

import mu.KLogger
import mu.KotlinLogging

/**
 * AbstractRequeryTest
 *
 * @author debop@coupang.com
 */
abstract class AbstractRequeryTest {

    companion object {
        val log: KLogger by lazy { KotlinLogging.logger { } }
    }
}