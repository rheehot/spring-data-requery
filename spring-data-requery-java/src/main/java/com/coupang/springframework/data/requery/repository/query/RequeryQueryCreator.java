package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.mapping.RequeryMappingContext;
import com.coupang.springframework.data.requery.repository.query.ParameterMetadataProvider.ParameterMetadata;
import com.coupang.springframework.data.requery.utils.RequeryUtils;
import io.requery.query.Condition;
import io.requery.query.NamedExpression;
import io.requery.query.Result;
import io.requery.query.WhereAndOr;
import io.requery.query.element.QueryElement;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.repository.query.ReturnedType;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.springframework.data.repository.query.parser.Part.Type.LIKE;
import static org.springframework.data.repository.query.parser.Part.Type.NOT_CONTAINING;

/**
 * Query creator to create a {@link io.requery.sql.QueryBuilder} from a {@link PartTree}.
 *
 * @author debop
 * @since 18. 6. 7
 */
@Slf4j
@Getter
public class RequeryQueryCreator extends AbstractQueryCreator<QueryElement<? extends Result<?>>, QueryElement<? extends Result<?>>> {

    private final RequeryOperations operations;
    private final RequeryMappingContext context;
    private final ReturnedType returnedType;
    private final Class<?> domainClass;
    private final String domainClassName;
    private final ParameterMetadataProvider provider;
    private final PartTree tree;
    private final RequeryParameterAccessor accessor;
    private final Object[] parameters;

    private final QueryElement<? extends Result<?>> root;

    public RequeryQueryCreator(@NotNull RequeryOperations operations,
                               @NotNull ParameterMetadataProvider provider,
                               @NotNull ReturnedType returnedType,
                               @NotNull PartTree tree,
                               @NotNull RequeryParameterAccessor accessor,
                               Object[] parameters) {
        super(tree, accessor);

        Assert.notNull(operations, "operation must not be null!");
        Assert.notNull(provider, "provider must not be null!");
        Assert.notNull(tree, "tree must not be null!");
        Assert.notNull(accessor, "accessor must not be null!");

        this.operations = operations;
        this.context = operations.getMappingContext();
        this.provider = provider;

        this.returnedType = returnedType;
        this.domainClass = returnedType.getDomainType();
        this.domainClassName = returnedType.getDomainType().getSimpleName();

        this.tree = tree;
        this.accessor = accessor;
        this.parameters = parameters;

        this.root = createQueryElement(returnedType);

        log.debug("Create RequeryQueryCreator for {}", domainClassName);
    }

    @SuppressWarnings("unchecked")
    protected QueryElement<? extends Result<?>> createQueryElement(ReturnedType type) {

        log.debug("Create QueryElement instance. ReturnedType={}", type);

        Class<?> typeToRead = type.getTypeToRead();
        return typeToRead == null || tree.isExistsProjection()
               ? (QueryElement<? extends Result<?>>) operations.select().from(type.getDomainType())
               : (QueryElement<? extends Result<?>>) operations.select(typeToRead);
    }

    public List<ParameterMetadata<?>> getParameterExpressions() {
        return provider.getExpressions();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected QueryElement<? extends Result<?>> create(Part part, Iterator<Object> iterator) {
        return toQueryElement(part, root);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected QueryElement<? extends Result<?>> and(Part part,
                                                    QueryElement<? extends Result<?>> base,
                                                    Iterator<Object> iterator) {
        return (QueryElement<? extends Result<?>>) (((WhereAndOr<?>) base).and((Condition<?, ?>) toQueryElement(part, root)));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected QueryElement<? extends Result<?>> or(QueryElement<? extends Result<?>> base,
                                                   QueryElement<? extends Result<?>> criteria) {
        return (QueryElement<? extends Result<?>>) (((WhereAndOr<?>) base).or((Condition<?, ?>) criteria));
    }

    @Override
    protected QueryElement<? extends Result<?>> complete(QueryElement<? extends Result<?>> criteria, Sort sort) {
        return complete(criteria, sort, root);
    }

    @SuppressWarnings("unchecked")
    protected QueryElement<? extends Result<?>> complete(QueryElement<? extends Result<?>> criteria,
                                                         Sort sort,
                                                         QueryElement<? extends Result<?>> root) {
        QueryElement<?> queryElement = criteria != null ? criteria : root;

        return RequeryUtils.applySort(returnedType.getDomainType(),
                                      (QueryElement<? extends Result<?>>) queryElement,
                                      sort);
    }

    private QueryElement<? extends Result<?>> toQueryElement(Part part, QueryElement<? extends Result<?>> root) {
        return new QueryElementBuilder(part, root).build();
    }

    private class QueryElementBuilder {

        private final Part part;
        private final QueryElement<? extends Result<?>> root;

        public QueryElementBuilder(Part part, QueryElement<? extends Result<?>> root) {
            Assert.notNull(part, "Part must not be null!");
            Assert.notNull(root, "Root must not be null!");
            this.part = part;
            this.root = root;
        }

        /**
         * Build Requery {@link QueryElement} from the underlying {@link Part}
         */
        @SuppressWarnings("unchecked")
        public QueryElement<? extends Result<?>> build() {

            PropertyPath property = part.getProperty();
            Part.Type type = part.getType();

            NamedExpression expr = NamedExpression.of(part.getProperty().getSegment(), part.getProperty().getType());

            log.debug("Build QueryElement ... property={}, type={}", property, type);

            switch (type) {
                case BETWEEN:
                    ParameterMetadata<Comparable> first = provider.next(part);
                    ParameterMetadata<Comparable> second = provider.next(part);

                    return (QueryElement<? extends Result<?>>) root.where(expr.between(first.getExpression(), second.getExpression()));

                case AFTER:
                case GREATER_THAN:
                    return (QueryElement<? extends Result<?>>) root.where(expr.greaterThan(provider.next(part, Comparable.class).getExpression()));

                case GREATER_THAN_EQUAL:
                    return (QueryElement<? extends Result<?>>) root.where(expr.greaterThanOrEqual(provider.next(part, Comparable.class).getExpression()));

                case BEFORE:
                case LESS_THAN:
                    return (QueryElement<? extends Result<?>>) root.where(expr.lt(provider.next(part, Comparable.class).getExpression()));

                case LESS_THAN_EQUAL:
                    return (QueryElement<? extends Result<?>>) root.where(expr.lte(provider.next(part, Comparable.class).getExpression()));

                case IS_NULL:
                    return (QueryElement<? extends Result<?>>) root.where(expr.isNull());

                case IS_NOT_NULL:
                    return (QueryElement<? extends Result<?>>) root.where(expr.notNull());

                case NOT_IN:
                    return (QueryElement<? extends Result<?>>) root.where(expr.notIn(provider.next(part, Collection.class).getExpression()));

                case IN:
                    return (QueryElement<? extends Result<?>>) root.where(expr.in(provider.next(part, Collection.class).getExpression()));

                case STARTING_WITH:
                case ENDING_WITH:
                case CONTAINING:
                case NOT_CONTAINING:

                    if (property.getLeafProperty().isCollection()) {
                        throw new NotImplementedException("구현 중");
                    }

                case LIKE:
                case NOT_LIKE:
                    String parameter = (String) provider.next(part, String.class).getValue();
                    return (type.equals(LIKE) || type.equals(NOT_CONTAINING))
                           ? (QueryElement<? extends Result<?>>) root.where(expr.like(parameter))
                           : (QueryElement<? extends Result<?>>) root.where(expr.notLike(parameter));

                case TRUE:
                    return (QueryElement<? extends Result<?>>) root.where(expr.eq(true));

                case FALSE:
                    return (QueryElement<? extends Result<?>>) root.where(expr.eq(false));

                case SIMPLE_PROPERTY:
                    ParameterMetadata<Object> expression = provider.next(part);
                    return (QueryElement<? extends Result<?>>) (
                        expression.isIsNullParameter()
                        ? root.where(expr.isNull())
                        : root.where(expr.eq(expression))
                    );

                case NEGATING_SIMPLE_PROPERTY:
                    return (QueryElement<? extends Result<?>>) root.where(expr.notEqual(provider.next(part).getExpression()));

                case IS_EMPTY:

                    if (!property.getLeafProperty().isCollection()) {
                        throw new IllegalArgumentException("IsEmpty / IsNotEmpty can only be used on collection properties!");
                    }
                    // NOTE: association 관련이므로, 명시적인 JOIN 구문을 사용해야 합니다.
                    return root;

                default:
                    throw new IllegalArgumentException("Unsupported keyword " + type);
            }
        }
    }
}
