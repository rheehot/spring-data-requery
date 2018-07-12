package org.springframework.data.requery;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Annotation Utility class
 *
 * NOTE: Java API 를 Kotlin 에서 호출 시에 ambiguous type error 가 발생할 때가 있다.
 * NOTE: 이 때에는 원하는 메소드만 Java Code 로 구현해서, Kotlin에서 호출하면 됩니다.
 *
 * @author debop@coupang.com
 */
public final class Annotations {

    private Annotations() {}

    @Nullable
    public static <A extends Annotation> A findAnnotation(@NotNull Class<?> clazz, @NotNull Class<A> annotationType) {
        return AnnotationUtils.findAnnotation(clazz, annotationType);
    }

    @Nullable
    public static <A extends Annotation> A findAnnotation(@NotNull Method method, @NotNull Class<A> annotationType) {
        return AnnotationUtils.findAnnotation(method, annotationType);
    }
}
