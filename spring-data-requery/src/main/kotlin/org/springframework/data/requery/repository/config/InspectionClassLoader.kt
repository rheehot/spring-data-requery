package org.springframework.data.requery.repository.config

import org.springframework.instrument.classloading.ShadowingClassLoader

/**
 * Disposable [ClassLoader] used to inspect user-code classes within an isolated class loader without preventing
 * class transformation at a later time.
 *
 * @author debop
 */
class InspectionClassLoader(parent: ClassLoader): ShadowingClassLoader(parent, true) {

    init {
        excludePackage("org.springframework.")
    }
}