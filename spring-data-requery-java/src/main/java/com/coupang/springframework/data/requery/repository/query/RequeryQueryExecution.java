package com.coupang.springframework.data.requery.repository.query;

import org.jetbrains.annotations.Nullable;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.ClassUtils;

import java.util.Collection;
import java.util.Optional;

/**
 * Set of classes to contain query execution strategies. Depending (mostly) on the return type of a
 * {@link org.springframework.data.repository.query.QueryMethod} a {@link AbstractStringBasedRequeryQuery} can be executed
 * in various flavors.
 *
 * @author debop
 * @since 18. 6. 7
 */
public abstract class RequeryQueryExecution {

    private static final ConversionService CONVERSION_SERVICE;

    static {

        ConfigurableConversionService conversionService = new DefaultConversionService();

        // Blob to Byte array 로 하는 것은 BlobByteArrayConverter 를 사용하면 된다.
        // conversionService.addConverter(JpaResultConverters.BlobToByteArrayConverter.INSTANCE);

        conversionService.removeConvertible(Collection.class, Object.class);
        potentiallyRemoveOptionalConverter(conversionService);

        CONVERSION_SERVICE = conversionService;
    }

    /**
     * Executes the given {@link AbstractStringBasedRequeryQuery} with the given {@link ParameterBinder}.
     *
     * @param query
     * @param values
     * @return
     */
    @Nullable
    public Object execute(AbstractRequeryQuery query, Object[] values) {

    }

    /**
     * Removes the converter being able to convert any object into an {@link Optional} from the given
     * {@link ConversionService} in case we're running on Java 8.
     *
     * @param conversionService must not be {@literal null}.
     */
    public static void potentiallyRemoveOptionalConverter(ConfigurableConversionService conversionService) {

        ClassLoader classLoader = RequeryQueryExecution.class.getClassLoader();

        if (ClassUtils.isPresent("java.util.Optional", classLoader)) {
            try {
                Class<?> optionalType = ClassUtils.forName("java.util.Optional", classLoader);
                conversionService.removeConvertible(Object.class, optionalType);
            } catch (ClassNotFoundException | LinkageError o_O) {
                // Nothing to do.
            }
        }
    }
}
