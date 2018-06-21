package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import io.requery.query.Result;
import io.requery.query.element.QueryElement;
import io.requery.query.function.Count;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.ReturnedType;
import org.springframework.data.repository.query.parser.PartTree;

/**
 * com.coupang.springframework.data.requery.repository.query.RequeryCountQueryCreator
 *
 * @author debop
 * @since 18. 6. 21
 */
@Slf4j
public class RequeryCountQueryCreator extends RequeryQueryCreator {

    public RequeryCountQueryCreator(@NotNull RequeryOperations operations,
                                    @NotNull ParameterMetadataProvider provider,
                                    @NotNull ReturnedType returnedType,
                                    @NotNull PartTree tree,
                                    @NotNull RequeryParameterAccessor accessor,
                                    Object[] values) {
        super(operations, provider, returnedType, tree, accessor, values);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected QueryElement<? extends Result<?>> createQueryElement(ReturnedType type) {
        return (QueryElement<? extends Result<?>>) getOperations().select().from(type.getDomainType());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected QueryElement<? extends Result<?>> complete(QueryElement<? extends Result<?>> criteria,
                                                         Sort sort,
                                                         QueryElement<? extends Result<?>> root) {

        QueryElement<? extends Result<?>> query = (QueryElement<? extends Result<?>>) getOperations().select(Count.count(getDomainClass()));

        if (criteria.isDistinct()) {
            query = (QueryElement<? extends Result<?>>) query.distinct();
        }

        return (QueryElement<? extends Result<?>>) query.where().exists(criteria);
    }


}
