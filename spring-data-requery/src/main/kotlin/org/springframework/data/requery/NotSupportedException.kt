package org.springframework.data.requery

/**
 * NotSupportedException
 *
 * @author debop@coupang.com
 * @since 18. 7. 2
 */
class NotSupportedException: RuntimeException {

    constructor(): super()
    constructor(message: String): super(message)
    constructor(message: String, cause: Throwable): super(message, cause)
    constructor(cause: Throwable): super(cause)
}