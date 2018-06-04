package com.coupang.springframework.data.requery.domain.functional

import java.util.function.Supplier

/**
 * PhoneFactory
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
class PhoneFactory: Supplier<FuncPhone> {
    override fun get(): FuncPhone = FuncPhone().apply {
        phoneNumber = ""
        extensions = IntArray(5) { 300 + it }
    }
}