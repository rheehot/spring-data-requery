package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.annotation.Query;
import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.provider.QueryExtractor;
import com.coupang.springframework.data.requery.provider.RequeryPersistenceProvider;
import io.requery.sql.EntityDataStore;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.RepositoryQuery;

import java.lang.reflect.Method;

/**
 * Query lookup strategy to execute finders.
 *
 * @author debop
 * @since 18. 6. 9
 */
@Slf4j
@UtilityClass
public final class RequeryQueryLookupStrategy {

    public static QueryLookupStrategy create(RequeryOperations operations,
                                             QueryLookupStrategy.Key key,
                                             QueryExtractor extractor,
                                             EvaluationContextProvider evaluationContextProvider) {
        log.debug("Create Query Lookup Strategy with key={}", key);

        switch (key != null ? key : QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND) {
            case CREATE:
                log.trace("Create CreateQueryLookupStrategy instance.");
                return new CreateQueryLookupStrategy(operations, extractor);

            case USE_DECLARED_QUERY:
                log.trace("Create DeclaredQueryLookupStrategy instance.");
                return new DeclaredQueryLookupStrategy(operations, extractor, evaluationContextProvider);

            case CREATE_IF_NOT_FOUND:
                log.trace("Create CreateIfNotFoundQueryLookupStrategy instance.");
                return new CreateIfNotFoundQueryLookupStrategy(operations,
                                                               extractor,
                                                               new CreateQueryLookupStrategy(operations, extractor),
                                                               new DeclaredQueryLookupStrategy(operations, extractor, evaluationContextProvider));
            default:
                throw new IllegalArgumentException("Unsupported query lookup strategy " + key);
        }
    }

    /**
     * Base class for {@link QueryLookupStrategy} implementations that need access to an {@link EntityDataStore}.
     */
    private abstract static class AbstractQueryLookupStrategy implements QueryLookupStrategy {

        private final RequeryOperations operations;
        private final QueryExtractor extractor;

        public AbstractQueryLookupStrategy(RequeryOperations operations, QueryExtractor extractor) {
            this.operations = operations;
            this.extractor = extractor;
        }

        @Override
        public final RepositoryQuery resolveQuery(Method method,
                                                  RepositoryMetadata metadata,
                                                  ProjectionFactory factory,
                                                  NamedQueries namedQueries) {
            return resolveQuery(new RequeryQueryMethod(method, metadata, factory, extractor), operations, namedQueries);
        }

        protected abstract RepositoryQuery resolveQuery(RequeryQueryMethod method, RequeryOperations operations, NamedQueries namedQueries);
    }


    /**
     * {@link QueryLookupStrategy} to create a query from the queryMethod name.
     */
    private static class CreateQueryLookupStrategy extends AbstractQueryLookupStrategy {

        private final RequeryPersistenceProvider persistenceProvider;


        public CreateQueryLookupStrategy(RequeryOperations operations, QueryExtractor extractor) {
            super(operations, extractor);
            persistenceProvider = new RequeryPersistenceProvider(operations);
        }

        @Override
        protected RepositoryQuery resolveQuery(RequeryQueryMethod method,
                                               RequeryOperations operations,
                                               NamedQueries namedQueries) {
            log.debug("Create PartTreeRequeryQuery, queryMethod={}", method);
            return new PartTreeRequeryQuery(method, operations, persistenceProvider);
        }
    }

    /**
     * {@link QueryLookupStrategy} that tries to detect a declared query declared via {@link Query} annotation followed by
     * a Requery named query lookup.
     */
    private static class DeclaredQueryLookupStrategy extends AbstractQueryLookupStrategy {

        private final EvaluationContextProvider evaluationContextProvider;

        public DeclaredQueryLookupStrategy(RequeryOperations operations,
                                           QueryExtractor extractor,
                                           EvaluationContextProvider evaluationContextProvider) {
            super(operations, extractor);
            this.evaluationContextProvider = evaluationContextProvider;
        }

        @Override
        protected RepositoryQuery resolveQuery(RequeryQueryMethod method,
                                               RequeryOperations operations,
                                               NamedQueries namedQueries) {
            // @Query annotation이 있다면 그 값으로 한다.
            if (method.isAnnotatedQuery()) {
                log.debug("Create DeclaredRequeryQuery for @Query annotated method. queryMethod={}", method.getName());
                return new DeclaredRequeryQuery(method, operations);
            }

            // Interface default method라면 그 함수를 그대로 사용한다.
            if (method.isDefaultMethod()) {
                log.debug("Create RawStringRequeryQuery for inteface default method. queryMethod={}", method.getName());
                return new DeclaredRequeryQuery(method, operations);
            }

            // Custom implemented method 라면 그 함수를 그대로 사용한다.
            if (method.isOverridedMethod()) {
                log.debug("Create DeclaredRequeryQuery for custom implmented method. queryMethod={}", method.getName());
                return new DeclaredRequeryQuery(method, operations);
            }

            throw new IllegalStateException(
                String.format("Cannot find a annotated query for method %s!", method)
            );
        }
    }

    /**
     * {@link QueryLookupStrategy} to try to detect a declared query first (
     * {@link Query}, Requery named query). In case none is found we fall back on
     * query creation.
     */
    private static class CreateIfNotFoundQueryLookupStrategy extends AbstractQueryLookupStrategy {

        private final DeclaredQueryLookupStrategy lookupStrategy;
        private final CreateQueryLookupStrategy createStrategy;

        public CreateIfNotFoundQueryLookupStrategy(RequeryOperations operations,
                                                   QueryExtractor extractor,
                                                   CreateQueryLookupStrategy createStrategy,
                                                   DeclaredQueryLookupStrategy lookupStrategy) {
            super(operations, extractor);
            this.createStrategy = createStrategy;
            this.lookupStrategy = lookupStrategy;
        }

        @Override
        protected RepositoryQuery resolveQuery(RequeryQueryMethod method,
                                               RequeryOperations operations,
                                               NamedQueries namedQueries) {

            log.debug("Resolve query ... queryMethod={}, namedQueries={}", method, namedQueries);

            try {
                log.debug("Resolve query by DeclaredQueryLookupStrategy...");
                return lookupStrategy.resolveQuery(method, operations, namedQueries);
            } catch (IllegalStateException e) {
                log.debug("Resolve query by CreateQueryLookupStrategy...");
                return createStrategy.resolveQuery(method, operations, namedQueries);
            }
        }
    }


}
