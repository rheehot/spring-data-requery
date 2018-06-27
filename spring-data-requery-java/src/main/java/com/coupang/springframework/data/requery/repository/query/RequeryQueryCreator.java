package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.NotSupportedException;
import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.mapping.RequeryMappingContext;
import com.coupang.springframework.data.requery.repository.query.ParameterMetadataProvider.ParameterMetadata;
import com.coupang.springframework.data.requery.utils.RequeryUtils;
import io.requery.query.Condition;
import io.requery.query.FieldExpression;
import io.requery.query.NamedExpression;
import io.requery.query.Return;
import io.requery.query.element.QueryElement;
import io.requery.query.function.Count;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.repository.query.ReturnedType;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.coupang.springframework.data.requery.utils.RequeryUtils.unwrap;
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
public class RequeryQueryCreator extends AbstractQueryCreator<QueryElement<?>, QueryElement<?>> {

    private final RequeryOperations operations;
    private final RequeryMappingContext context;
    private final ReturnedType returnedType;
    private final Class<?> domainClass;
    private final String domainClassName;
    private final ParameterMetadataProvider provider;
    private final PartTree tree;
//    private final RequeryParameterAccessor accessor;
//    private final Object[] parameters;

    private final QueryElement<?> root;

    public RequeryQueryCreator(@NotNull RequeryOperations operations,
                               @NotNull ParameterMetadataProvider provider,
                               @NotNull ReturnedType returnedType,
                               @NotNull PartTree tree) {
        super(tree);

        Assert.notNull(operations, "operation must not be null!");
        Assert.notNull(provider, "provider must not be null!");
        Assert.notNull(tree, "tree must not be null!");

        this.operations = operations;
        this.context = operations.getMappingContext();
        this.provider = provider;

        this.returnedType = returnedType;
        this.domainClass = returnedType.getDomainType();
        this.domainClassName = returnedType.getDomainType().getSimpleName();

        this.tree = tree;
//        this.accessor = accessor;
//        this.parameters = parameters;

        this.root = createQueryElement(returnedType);

        log.debug("Create RequeryQueryCreator for [{}]", domainClassName);
    }

    @SuppressWarnings("unchecked")
    protected QueryElement<?> createQueryElement(ReturnedType type) {

        Assert.notNull(type, "type must not be null!");

        Class<?> typeToRead = type.getTypeToRead();

        log.debug("Create QueryElement instance. ReturnedType={}, typeToRead={}", type, typeToRead);

        // TODO: READ 뿐 아니라 insert/update/upsert/delete 시에는 QueryElement가 달라야 한다.
        // TODO: 근데 이를 어떻게 알지?

        if (tree.isCountProjection()) {
            return unwrap(operations.select(Count.count(type.getDomainType())));
        }
        if (tree.isExistsProjection()) {
            return unwrap(operations.select(type.getDomainType()));
        }
        if (tree.isDelete()) {
            return unwrap(operations.delete(type.getDomainType()));
        }

        return unwrap(operations.select(type.getDomainType()));
    }

    public List<ParameterMetadata<?>> getParameterExpressions() {
        return provider.getExpressions();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected QueryElement<?> create(Part part, Iterator<Object> iterator) {
        return toQueryElement(part, root);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected QueryElement<?> and(Part part,
                                  QueryElement<?> base,
                                  Iterator<Object> iterator) {

        return RequeryUtils.buildWhereClause(base,
                                             Collections.singletonList((Condition<?, ?>) toQueryElement(part, root).getWhereElements().iterator().next().getCondition()),
                                             true);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected QueryElement<?> or(QueryElement<?> base,
                                 QueryElement<?> criteria) {

        Iterable<Condition<?, ?>> conditions = RequeryUtils.getConditions(criteria);
        return unwrap(RequeryUtils.buildWhereClause(base, conditions, false));
        // return RequeryUtils.unwrap(base.where().or((Condition<?, ?>) criteria));
    }

    @Override
    protected QueryElement<?> complete(QueryElement<?> criteria, Sort sort) {
        return complete(criteria, sort, root);
    }

    @SuppressWarnings("unchecked")
    protected QueryElement<?> complete(QueryElement<?> criteria,
                                       Sort sort,
                                       QueryElement<?> root) {
        QueryElement<?> queryElement = criteria != null ? criteria : root;

        return RequeryUtils.applySort(returnedType.getDomainType(),
                                      queryElement,
                                      sort);
    }

    private QueryElement<?> toQueryElement(Part part, QueryElement<?> root) {
        return new QueryElementBuilder(part, root).build();
    }

    private class QueryElementBuilder {

        private final Part part;
        private final QueryElement<?> root;

        public QueryElementBuilder(Part part, QueryElement<?> root) {
            Assert.notNull(part, "Part must not be null!");
            Assert.notNull(root, "Root must not be null!");
            this.part = part;
            this.root = root;
        }

        /**
         * Build Requery {@link QueryElement} from the underlying {@link Part}
         */
        @SuppressWarnings("unchecked")
        public QueryElement<?> build() {

            PropertyPath property = part.getProperty();
            Part.Type type = part.getType();

            NamedExpression expr = NamedExpression.of(part.getProperty().getSegment(), part.getProperty().getType());

            log.debug("Build QueryElement ... property={}, type={}", property, type);

            Return<?> whereClause;

            switch (type) {
                case BETWEEN:
                    ParameterMetadata<Comparable> first = provider.next(part);
                    ParameterMetadata<Comparable> second = provider.next(part);

                    whereClause = root.where(expr.between(first.getExpression(), second.getExpression()));
                    break;

                case AFTER:
                case GREATER_THAN:
                    whereClause = root.where(expr.greaterThan(provider.next(part, Comparable.class).getExpression()));
                    break;

                case GREATER_THAN_EQUAL:
                    whereClause = root.where(expr.greaterThanOrEqual(provider.next(part, Comparable.class).getExpression()));
                    break;

                case BEFORE:
                case LESS_THAN:
                    whereClause = root.where(expr.lt(provider.next(part, Comparable.class).getExpression()));
                    break;

                case LESS_THAN_EQUAL:
                    whereClause = root.where(expr.lte(provider.next(part, Comparable.class).getExpression()));
                    break;

                case IS_NULL:
                    whereClause = root.where(expr.isNull());
                    break;

                case IS_NOT_NULL:
                    whereClause = root.where(expr.notNull());
                    break;

                case NOT_IN:
                    whereClause = root.where(expr.notIn(provider.next(part, Collection.class).getExpression()));
                    break;

                case IN:
                    whereClause = root.where(expr.in(provider.next(part, Collection.class).getExpression()));
                    break;

                case STARTING_WITH:
                    if (property.getLeafProperty().isCollection()) {
                        throw new NotSupportedException("Not supported keyword " + type);
                    }

                    whereClause = root.where(expr.like(provider.next(part, String.class).getValue() + "%"));
                    break;
                case ENDING_WITH:
                    if (property.getLeafProperty().isCollection()) {
                        throw new NotSupportedException("Not supported keyword " + type);
                    }

                    whereClause = root.where(expr.like("%" + provider.next(part, String.class).getValue()));
                    break;

                case CONTAINING:
                    if (property.getLeafProperty().isCollection()) {
                        throw new NotSupportedException("Not supported keyword " + type);
                    }

                    whereClause = root.where(expr.like("%" + provider.next(part, String.class).getValue() + "%"));
                    break;

                case NOT_CONTAINING:
                    if (property.getLeafProperty().isCollection()) {
                        throw new NotSupportedException("Not supported keyword " + type);
                    }

                    whereClause = root.where(expr.notLike("%" + provider.next(part, String.class).getValue() + "%"));
                    break;

                case LIKE:
                case NOT_LIKE:
                    FieldExpression<String> fieldExpr = upperIfIgnoreCase(expr);
                    Object paramValue = provider.next(part, String.class).getValue();
                    String value = (paramValue != null) ? paramValue.toString() : null;

                    whereClause = (type.equals(LIKE) || type.equals(NOT_CONTAINING))
                                  ? root.where(fieldExpr.like(value))
                                  : root.where(fieldExpr.notLike(value));
                    break;

                case TRUE:
                    whereClause = root.where(expr.eq(true));
                    break;

                case FALSE:
                    whereClause = root.where(expr.eq(false));
                    break;

                // IS, Equals 
                case SIMPLE_PROPERTY:
                    ParameterMetadata<Object> simpleExpr = provider.next(part);
                    upperIfIgnoreCase(simpleExpr.getExpression());

                    whereClause = simpleExpr.isIsNullParameter()
                                  ? root.where(expr.isNull())
                                  : root.where(upperIfIgnoreCase(expr).eq(simpleExpr.getValue()));
                    break;

                case NEGATING_SIMPLE_PROPERTY:
                    ParameterMetadata<Object> simpleNotExpr = provider.next(part);
                    upperIfIgnoreCase(simpleNotExpr.getExpression());
                    whereClause = root.where(upperIfIgnoreCase(expr).notEqual(simpleNotExpr.getValue()));
                    break;

                case IS_EMPTY:
                case IS_NOT_EMPTY:
                default:
                    throw new NotSupportedException("Not supported keyword " + type);
            }

            return unwrap(whereClause);
        }

        private <T> FieldExpression<T> upperIfIgnoreCase(FieldExpression<T> expression) {

            switch (part.shouldIgnoreCase()) {
                case ALWAYS:
                    Assert.state(canUpperCase(expression), "Unable to ignore case of " + expression.getClassType().getName()
                                                           + " types, the property '" + part.getProperty().getSegment() + "' must reference a String");
                    return expression.function("Upper");

                case WHEN_POSSIBLE:
                    if (canUpperCase(expression)) {
                        return expression.function("Upper");
                    }

                case NEVER:
                default:
                    return expression;
            }
        }

        private boolean canUpperCase(FieldExpression<?> expression) {
            return String.class.equals(expression.getClassType());
        }
    }
}
