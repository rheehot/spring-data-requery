package org.springframework.data.requery

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