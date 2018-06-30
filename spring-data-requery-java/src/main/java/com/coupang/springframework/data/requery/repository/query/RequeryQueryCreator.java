package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.NotSupportedException;
import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.mapping.RequeryMappingContext;
import com.coupang.springframework.data.requery.repository.query.ParameterMetadataProvider.ParameterMetadata;
import com.coupang.springframework.data.requery.utils.Iterables;
import io.requery.query.Condition;
import io.requery.query.FieldExpression;
import io.requery.query.LogicalCondition;
import io.requery.query.NamedExpression;
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static com.coupang.springframework.data.requery.utils.RequeryUtils.applySort;
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
public class RequeryQueryCreator extends AbstractQueryCreator<QueryElement<?>, LogicalCondition<?, ?>> {

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
    protected LogicalCondition<?, ?> create(Part part, Iterator<Object> iterator) {
        log.trace("Build new condition...");
        return buildWhereCondition(part);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected LogicalCondition<?, ?> and(Part part,
                                         LogicalCondition<?, ?> base,
                                         Iterator<Object> iterator) {
        log.trace("add AND condition");

        Condition<?, ?> condition = buildWhereCondition(part);

        log.trace("add where criteria. criteria={}", condition);

        return base.and(condition);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected LogicalCondition<?, ?> or(LogicalCondition<?, ?> base,
                                        LogicalCondition<?, ?> criteria) {
        return base.or(criteria);
    }

    @Override
    protected QueryElement<?> complete(LogicalCondition<?, ?> criteria, Sort sort) {
        return complete(criteria, sort, root);
    }

    @SuppressWarnings("unchecked")
    protected QueryElement<?> complete(LogicalCondition<?, ?> criteria,
                                       Sort sort,
                                       QueryElement<?> root) {
        log.trace("Complete query...");

        // TODO: returnType.needsCustomConstruction() 을 이용해서 Custom Type 에 대한 작업을 수행할 수 있다.

        QueryElement<?> queryElement = criteria != null ? unwrap(root.where(criteria)) : root;
        return applySort(returnedType.getDomainType(), queryElement, sort);
    }

    private LogicalCondition<?, ?> buildWhereCondition(Part part) {
        return new QueryElementBuilder(part).build();
    }

    private class QueryElementBuilder {

        private final Part part;

        public QueryElementBuilder(Part part) {
            log.debug("Create QueryElementBuilder. part={}l", part);

            Assert.notNull(part, "Part must not be null!");
            this.part = part;
        }

        /**
         * Build Requery {@link QueryElement} from the underlying {@link Part}
         */
        @SuppressWarnings("unchecked")
        public LogicalCondition<?, ?> build() {

            PropertyPath property = part.getProperty();
            Part.Type type = part.getType();

            NamedExpression expr = NamedExpression.of(part.getProperty().getSegment(), part.getProperty().getType());

            log.debug("Build QueryElement ... property={}, type={}, expr={}", property, type, expr);

            LogicalCondition<?, ?> condition;

            switch (type) {
                case BETWEEN:
                    ParameterMetadata<Comparable> first = provider.next(part);
                    ParameterMetadata<Comparable> second = provider.next(part);

                    condition = expr.between(first.getValue(), second.getValue());
                    break;

                case AFTER:
                case GREATER_THAN:
                    condition = expr.greaterThan(provider.next(part, Comparable.class).getValue());
                    break;

                case GREATER_THAN_EQUAL:
                    condition = expr.greaterThanOrEqual(provider.next(part, Comparable.class).getValue());
                    break;

                case BEFORE:
                case LESS_THAN:
                    condition = expr.lt(provider.next(part, Comparable.class).getValue());
                    break;

                case LESS_THAN_EQUAL:
                    condition = expr.lte(provider.next(part, Comparable.class).getValue());
                    break;

                case IS_NULL:
                    condition = expr.isNull();
                    break;

                case IS_NOT_NULL:
                    condition = expr.notNull();
                    break;

                case NOT_IN:
                case IN:
                    Object values = provider.next(part, Collection.class).getValue();
                    // log.trace("in ({}), class={}", values, values.getClass());

                    if (values instanceof Iterable) {
                        Collection cols = Iterables.toList((Iterable) values);
                        condition = (type == IN) ? expr.in(cols) : expr.notIn(cols);
                    } else if (values instanceof Object[]) {
                        List list = Arrays.asList((Object[]) values);
                        condition = (type == IN) ? expr.in(list) : expr.notIn(list);
                    } else {
                        condition = (type == IN) ? expr.in(values) : expr.notIn(values);
                    }
                    break;

                case STARTING_WITH:
                    if (property.getLeafProperty().isCollection()) {
                        throw new NotSupportedException("Not supported keyword " + type);
                    }

                    condition = expr.like(provider.next(part, String.class).getValue() + "%");
                    break;

                case ENDING_WITH:
                    if (property.getLeafProperty().isCollection()) {
                        throw new NotSupportedException("Not supported keyword " + type);
                    }

                    condition = expr.like("%" + provider.next(part, String.class).getValue());
                    break;

                case CONTAINING:
                    if (property.getLeafProperty().isCollection()) {
                        throw new NotSupportedException("Not supported keyword " + type);
                    }

                    condition = expr.like("%" + provider.next(part, String.class).getValue() + "%");
                    break;

                case NOT_CONTAINING:
                    if (property.getLeafProperty().isCollection()) {
                        throw new NotSupportedException("Not supported keyword " + type);
                    }

                    condition = expr.notLike("%" + provider.next(part, String.class).getValue() + "%");
                    break;

                case LIKE:
                case NOT_LIKE:
                    FieldExpression<String> fieldExpr = upperIfIgnoreCase(expr);
                    Object paramValue = provider.next(part, String.class).getValue();
                    String value = (paramValue != null) ? paramValue.toString() : "";
                    if (shouldIgnoreCase()) {
                        value = value.toUpperCase();
                    }
                    if (!value.startsWith("%") && !value.endsWith("%")) {
                        value = "%" + value + "%";
                    }
                    condition = (type.equals(LIKE) || type.equals(CONTAINING))
                                ? fieldExpr.like(value)
                                : fieldExpr.notLike(value);
                    break;

                case TRUE:
                    condition = expr.eq(true);
                    break;

                case FALSE:
                    condition = expr.eq(false);
                    break;

                // IS, Equals 
                case SIMPLE_PROPERTY:
                    ParameterMetadata<Object> simpleExpr = provider.next(part);
                    upperIfIgnoreCase(simpleExpr.getExpression());

                    condition = simpleExpr.isIsNullParameter()
                                ? expr.isNull()
                                : upperIfIgnoreCase(expr).eq(shouldIgnoreCase()
                                                             ? upperCase(simpleExpr.getValue())
                                                             : simpleExpr.getValue());
                    break;

                case NEGATING_SIMPLE_PROPERTY:
                    ParameterMetadata<Object> simpleNotExpr = provider.next(part);
                    upperIfIgnoreCase(simpleNotExpr.getExpression());

                    condition = upperIfIgnoreCase(expr).notEqual(shouldIgnoreCase()
                                                                 ? upperCase(simpleNotExpr.getValue())
                                                                 : simpleNotExpr.getValue());
                    break;

                case IS_EMPTY:
                case IS_NOT_EMPTY:
                default:
                    throw new NotSupportedException("Not supported keyword " + type);
            }

            return condition;
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

        private boolean shouldIgnoreCase() {
            return part.shouldIgnoreCase() != Part.IgnoreCaseType.NEVER;
        }

        private String upperCase(Object value) {
            return value.toString().toUpperCase();
        }
    }
}
