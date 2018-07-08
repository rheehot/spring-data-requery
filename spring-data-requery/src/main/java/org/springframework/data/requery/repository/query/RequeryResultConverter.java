package org.springframework.data.requery.repository.query;

import io.requery.query.Tuple;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * RequeryResultConverter
 *
 * @author debop@coupang.com
 * @since 18. 6. 28
 */
@Slf4j
@UtilityClass
public class RequeryResultConverter {

    public static Object convertResult(Object result) {
        return convertResult(result, null);
    }

    public static Object convertResult(Object result, Object defaultValue) {

        log.trace("Convert result... result={}", result);
        try {
            if (result instanceof Tuple) {
                Tuple tuple = (Tuple) result;
                if (tuple.count() == 1)
                    return tuple.get(0);

                return tuple;
            }
            return result;

        } catch (Exception e) {
            log.warn("Fail to convert result[{}]. return [{}]", result, defaultValue);
            return defaultValue;
        }
    }
}
