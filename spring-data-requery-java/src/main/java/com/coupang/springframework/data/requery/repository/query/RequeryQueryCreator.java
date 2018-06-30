package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.NotSupportedException;
import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.mapping.RequeryMappingContext;
import com.coupang.springframework.data.requery.repository.query.ParameterMetadataProvider.ParameterMetadata;
import com.coupang.springframework.data.requery.utils.Iterables;
import com.coupang.springframework.data.requery.utils.RequeryUtils;
import io.requery.query.Condition;
import io.requery.query.FieldExpression;
import io.requery.query.NamedExpression;
import io.requery.query.Return;
import io.requery.query.element.QueryElement;
import io.requery.query.element.WhereConditionElement;
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

import java.util.*;

import static com.coupang.springframework.data.requery.utils.RequeryUtils.applyWhereClause;
import static com.coupang.springframework.data.requery.utils.RequeryUtils.unwrap;
import static org.springframework.data.repository.query.parser.Part.Type.*;

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
        this.root = createQueryElement(returnedType);

        log.debug("Create RequeryQueryCreator for [{}]", domainClassName);
    }

    @SuppressWarnings("unchecked")
    protected QueryElement<?> createQueryElement(ReturnedType type) {

        Assert.notNull(type, "type must not be null!");

        Class<?> typeToRead = type.getTypeToRead();

        log.debug("Create QueryElement instance. ReturnedType={}, typeToRead={}", type, typeToRead);

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
        log.trace("add AND condition");
        Set<WhereConditionElement<?>> elements = toQueryElement(part, root).getWhereElements();

        log.debug("elements size={}", elements.size());

        if (elements.size() > 0) {
            Condition<?, ?> condition = elements.iterator().next().getCondition();
            return unwrap(base.where(condition));
        } else {
            return unwrap(base);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected QueryElement<?> or(QueryElement<?> base,
                                 QueryElement<?> criteria) {

        log.trace("add OR condition. base condition size={}", base.getWhereElements().size());
        log.trace("add OR condition. criteria condition size={}", criteria.getWhereElements().size());

        base.getWhereElements().forEach(element -> {
            log.trace("base where element. operator={}, left operand={}", element.getOperator(), element.getCondition().getLeftOperand());
        });

        criteria.getWhereElements().forEach(element -> {
            log.trace("criteria where element. operator={}, left operand={}", element.getOperator(), element.getCondition().getLeftOperand());
        });

        Set<WhereConditionElement<?>> whereElements = criteria.getWhereElements();
        if (base == criteria) {
            whereElements = new HashSet(criteria.getWhereElements());
            base.getWhereElements().clear();
        }

        return unwrap(applyWhereClause(base, whereElements, false));
    }

    @Override
    protected QueryElement<?> complete(QueryElement<?> criteria, Sort sort) {
        return complete(criteria, sort, root);
    }

    @SuppressWarnings("unchecked")
    protected QueryElement<?> complete(QueryElement<?> criteria,
                                       Sort sort,
                                       QueryElement<?> root) {
        log.trace("Complete query...");

        QueryElement<?> queryElement = criteria != null ? criteria : root;
        return RequeryUtils.applySort(returnedType.getDomainType(), queryElement, sort);
    }

    private QueryElement<?> toQueryElement(Part part, QueryElement<?> root) {
        return new QueryElementBuilder(part, root).build();
    }


    private class QueryElementBuilder {

        private final Part part;
        private final QueryElement<?> root;

        public QueryElementBuilder(Part part, QueryElement<?> root) {
            log.debug("Create QueryElementBuilder. part={}, root={}", part, root);

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

            log.debug("Build QueryElement ... property={}, type={}, expr={}", property, type, expr);

            Return<?> whereClause;

            switch (type) {
                case BETWEEN:
                    ParameterMetadata<Comparable> first = provider.next(part);
                    ParameterMetadata<Comparable> second = provider.next(part);

                    whereClause = root.where(expr.between(first.getValue(), second.getValue()));
                    break;

                case AFTER:
                case GREATER_THAN:
                    whereClause = root.where(expr.greaterThan(provider.next(part, Comparable.class).getValue()));
                    break;

                case GREATER_THAN_EQUAL:
                    whereClause = root.where(expr.greaterThanOrEqual(provider.next(part, Comparable.class).getValue()));
                    break;

                case BEFORE:
                case LESS_THAN:
                    whereClause = root.where(expr.lt(provider.next(part, Comparable.class).getValue()));
                    break;

                case LESS_THAN_EQUAL:
                    whereClause = root.where(expr.lte(provider.next(part, Comparable.class).getValue()));
                    break;

                case IS_NULL:
                    whereClause = root.where(expr.isNull());
                    break;

                case IS_NOT_NULL:
                    whereClause = root.where(expr.notNull());
                    break;

                case NOT_IN:
                case IN:
                    Object values = provider.next(part, Collection.class).getValue();
                    log.trace("in ({}), class={}", values, values.getClass());

                    if (values instanceof Iterable) {
                        Collection cols = Iterables.toList((Iterable) values);
                        log.trace("cols = {}", cols);
                        whereClause = (type == IN) ? root.where(expr.in(cols)) : root.where(expr.notIn(cols));
                    } else if (values instanceof Object[]) {
                        List list = Arrays.asList((Object[]) values);
                        whereClause = (type == IN) ? root.where(expr.in(list)) : root.where(expr.notIn(list));
                    } else {
                        whereClause = (type == IN) ? root.where(expr.in(values)) : root.where(expr.notIn(values));
                    }
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
                    String value = (paramValue != null) ? paramValue.toString() : "";

                    if (!value.startsWith("%") && !value.endsWith("%")) {
                        value = "%" + value + "%";
                    }

                    whereClause = (type.equals(LIKE) || type.equals(CONTAINING))
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
