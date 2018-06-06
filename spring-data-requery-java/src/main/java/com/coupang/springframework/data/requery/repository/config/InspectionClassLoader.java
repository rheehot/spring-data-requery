package com.coupang.springframework.data.requery.repository.config;

import org.springframework.instrument.classloading.ShadowingClassLoader;

/**
 * Disposable {@link ClassLoader} used to inspect user-code classes within an isolated class loader without preventing
 * class transformation at a later time.
 *
 * @author debop
 * @since 18. 6. 6
 */
public class InspectionClassLoader extends ShadowingClassLoader {

    InspectionClassLoader(ClassLoader parent) {
        super(parent, true);
        excludePackage("org.springframework.");
    }
}