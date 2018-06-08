package com.coupang.springframework.data.requery.repository.query;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.Parameter;
import org.springframework.data.repository.query.Parameters;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * com.coupang.springframework.data.requery.repository.query.RequeryParameters
 *
 * @author debop
 * @since 18. 6. 7
 */
@Slf4j
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

        private final MethodParameter parameter;

        /**
         * Creates a new {@link Parameter} for the given {@link MethodParameter}.
         *
         * @param parameter must not be {@literal null}.
         */
        public RequeryParameter(MethodParameter parameter) {
            super(parameter);
            this.parameter = parameter;
        }

        @Override
        public Optional<String> getName() {
            Param annotation = parameter.getParameterAnnotation(Param.class);
            return Optional.ofNullable(annotation == null ? null : annotation.value());
        }
    }
}
