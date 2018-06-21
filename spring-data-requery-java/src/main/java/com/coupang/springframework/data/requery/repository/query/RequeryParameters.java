package com.coupang.springframework.data.requery.repository.query;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.data.repository.query.Parameter;
import org.springframework.data.repository.query.Parameters;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 메소드의 Parameter 정보를 나타냅니다.
 *
 * @author debop
 * @since 18. 6. 7
 */
@Slf4j
public class RequeryParameters extends Parameters<RequeryParameters, RequeryParameters.RequeryParameter> {

    public RequeryParameters(Method method) {
        super(method);
        log.debug("Ctor RequeryParameters. queryMethod={}", method);
    }

    public RequeryParameters(List<RequeryParameter> parameters) {
        super(parameters);
        log.debug("Ctor RequeryParameters. parameters={}", parameters);
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        getBindableParameters().forEach(param -> {
            builder.append(param.toString()).append(",");
        });

        return "(" + builder.substring(0, builder.length() - 1) + ")";
    }

    @Slf4j
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

            log.debug("Create RequeryParameter. parameter={}", parameter);
        }

        public boolean isDateParameter() {
            return Objects.equals(getType(), Date.class);
        }

        @Override
        public String toString() {
            return parameter.getParameter().toString();
        }
    }
}
