package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.annotation.Modifying;
import com.coupang.springframework.data.requery.annotation.QueryOptions;
import com.coupang.springframework.data.requery.provider.QueryExtractor;
import com.coupang.springframework.data.requery.annotation.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.Parameters;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Requery specific extension of {@link QueryMethod}.
 *
 * @author debop
 * @since 18. 6. 7
 */
@Slf4j
public class RequeryQueryMethod extends QueryMethod {

    private static final Set<Class<?>> NATIVE_ARRAY_TYPES;

    static {
        Set<Class<?>> types = new HashSet<>();
        types.add(byte[].class);
        types.add(Byte[].class);
        types.add(char[].class);
        types.add(Character[].class);

        NATIVE_ARRAY_TYPES = Collections.unmodifiableSet(types);
    }

    private final QueryExtractor extractor;
    private final Method method;

    /**
     * Creates a new {@link QueryMethod} from the given parameters. Looks up the correct query to use for following
     * invocations of the method given.
     *
     * @param method   must not be {@literal null}.
     * @param metadata must not be {@literal null}.
     * @param factory  must not be {@literal null}.
     */
    public RequeryQueryMethod(Method method, RepositoryMetadata metadata, ProjectionFactory factory, QueryExtractor extractor) {
        super(method, metadata, factory);

        Assert.notNull(method, "Method must not be null!");
        Assert.notNull(extractor, "Query extractor must not be null!");

        this.method = method;
        this.extractor = extractor;

        Assert.isTrue(!(isModifyingQuery() && getParameters().hasSpecialParameter()),
                      String.format("Modifying method must not contains %s!", Parameters.TYPES));

        assertParamterNamesInAnnotatedQuery();
    }

    private void assertParamterNamesInAnnotatedQuery() {
        throw new NotImplementedException("구현 중");
    }

    @NotNull
    @SuppressWarnings("unchecked")
    @Override
    public RequeryEntityMetadata<?> getEntityInformation() {
        return new DefaultRequeryEntityMetadata(getDomainClass());
    }

    @Override
    public boolean isModifyingQuery() {
        return null != AnnotationUtils.findAnnotation(method, Modifying.class);
    }


    QueryExtractor getQueryExtractor() {
        return extractor;
    }

    Class<?> getReturnType() {
        return method.getReturnType();
    }

    @Nullable
    String getAnnotatedQuery() {
        String query = getAnnotationValue("value", String.class);
        return StringUtils.hasText(query) ? query : null;
    }

    String getRequiredAnnotatedQuery() throws IllegalStateException {

        String query = getAnnotatedQuery();
        if (query != null) {
            return query;
        }

        throw new IllegalStateException(String.format("No annotated query found for query method %s!", getName()));
    }

    @Nullable
    String getCountQuery() {
        String countQuery = getAnnotationValue("countQuery", String.class);
        return StringUtils.hasText(countQuery) ? countQuery : null;
    }


    private String getAnnotationValue(String attribute, Class<String> type) {
        return getMergedOrDefaultAnnotationValue(attribute, Query.class, type);
    }

    private <T> T getMergedOrDefaultAnnotationValue(String attribute, Class annotationType, Class<T> targetType) {
        throw new NotImplementedException("구현 중");
    }

    @NotNull
    @Override
    protected RequeryParameters createParameters(Method method) {
        return new RequeryParameters(method);
    }

    @Override
    public RequeryParameters getParameters() {
        return (RequeryParameters) super.getParameters();
    }

    @Override
    public boolean isCollectionQuery() {
        return super.isCollectionQuery() && !NATIVE_ARRAY_TYPES.contains(method.getReturnType());
    }


    public RequeryQueryOptions getAnnotatedQueryOptions() {
        final QueryOptions queryOptions = getQueryOptionsAnnotation();
        if (queryOptions == null) {
            return null;
        }

        final RequeryQueryOptions options = new RequeryQueryOptions();
        options.setCache(queryOptions.cache());
        options.setCount(queryOptions.count());
        options.setFullCount(queryOptions.fullCount());

        return options;
    }

    public QueryOptions getQueryOptionsAnnotation() {
        return AnnotatedElementUtils.findMergedAnnotation(method, QueryOptions.class);
    }
}
