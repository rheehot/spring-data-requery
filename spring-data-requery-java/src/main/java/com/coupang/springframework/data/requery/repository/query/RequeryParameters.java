package com.coupang.springframework.data.requery.repository.query;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.data.repository.query.Parameter;
import org.springframework.data.repository.query.Parameters;

import java.lang.reflect.Method;
import java.util.List;

/**
 * com.coupang.springframework.data.requery.repository.query.RequeryParameters
 *
 * @author debop
 * @since 18. 6. 7
 */
public class RequeryParameters extends Parameters<RequeryParameters, RequeryParameters.RequeryParameter> {

    public RequeryParameters(Method method) {
        super(method);
    }

    public RequeryParameters(List<RequeryParameter> parameters) {
        super(parameters);
    }

    @NotNull
    @Override
    protected RequeryParameter createParameter(MethodParameter parameter) {
        return new RequeryParameter(parameter);
    }

    @NotNull
    @Override
    protected RequeryParameters createFrom(List<RequeryParameter> parameters) {
        return new RequeryParameters(parameters);
    }

    static class RequeryParameter extends Parameter {

        /**
         * Creates a new {@link Parameter} for the given {@link MethodParameter}.
         *
         * @param parameter must not be {@literal null}.
         */
        public RequeryParameter(MethodParameter parameter) {
            super(parameter);
        }
    }
}
