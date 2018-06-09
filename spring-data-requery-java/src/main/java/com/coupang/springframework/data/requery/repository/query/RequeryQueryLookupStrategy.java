package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.provider.QueryExtractor;
import com.coupang.springframework.data.requery.provider.RequeryPersistenceProvider;
import com.coupang.springframework.data.requery.repository.Query;
import io.requery.sql.EntityDataStore;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.NotImplementedException;
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
@UtilityClass
public final class RequeryQueryLookupStrategy {

    public static QueryLookupStrategy create(RequeryOperations operations,
                                             QueryLookupStrategy.Key key,
                                             QueryExtractor extractor,
                                             EvaluationContextProvider evaluationContextProvider) {
        switch (key != null ? key : QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND) {
            case CREATE:
                return new CreateQueryLookupStrategy(operations, extractor);
            case USE_DECLARED_QUERY:
                return new DeclaredQueryLookupStrategy(operations, extractor, evaluationContextProvider);
            case CREATE_IF_NOT_FOUND:
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
     * {@link QueryLookupStrategy} to create a query from the method name.
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
            throw new NotImplementedException("구현 중");
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
            try {
                return lookupStrategy.resolveQuery(method, operations, namedQueries);
            } catch (IllegalStateException e) {
                return createStrategy.resolveQuery(method, operations, namedQueries);
            }
        }
    }


}
