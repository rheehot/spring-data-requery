package org.springframework.data.requery;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Annotations
 *
 * @author debop@coupang.com
 */
public final class Annotations {

    private Annotations() {}

    @Nullable
    public static <A extends Annotation> A findAnnotation(@NotNull Method method, @NotNull Class<A> annotationType) {
        return AnnotationUtils.findAnnotation(method, annotationType);
    }
}
