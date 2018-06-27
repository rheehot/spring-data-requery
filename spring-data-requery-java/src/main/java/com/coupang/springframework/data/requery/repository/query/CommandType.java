package com.coupang.springframework.data.requery.repository.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * CommandType
 *
 * @author debop@coupang.com
 * @since 18. 6. 27
 */
@Slf4j
public enum CommandType {
    NONE,
    SELECT,
    INSERT,
    UPDATE,
    UPSERT,
    DELETE,
    REFRESH;


    public static CommandType parse(String methodName) {
        log.debug("Parse methodName={}", methodName);

        if (!StringUtils.hasText(methodName)) {
            return NONE;
        }
        String method = methodName.toLowerCase();

        if (method.startsWith("insert")) {
            return INSERT;
        }
        if (method.startsWith("update")) {
            return UPDATE;
        }
        if (method.startsWith("upsert")) {
            return UPSERT;
        }
        if (method.startsWith("delete") || method.startsWith("remove")) {
            return DELETE;
        }
        return SELECT;
    }
}
