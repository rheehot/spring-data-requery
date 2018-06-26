package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.utils.RequeryUtils;
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
                                    @NotNull PartTree tree) {
        super(operations, provider, returnedType, tree);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected QueryElement<? extends Result<?>> createQueryElement(ReturnedType type) {
        return (QueryElement<? extends Result<?>>) getOperations().select().from(type.getDomainType());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected QueryElement<?> complete(QueryElement<?> criteria,
                                       Sort sort,
                                       QueryElement<?> root) {

        QueryElement<? extends Result<?>> query = (QueryElement<? extends Result<?>>) getOperations().select(Count.count(getDomainClass()));

        if (criteria != null) {
            if (criteria.isDistinct()) {
                query = (QueryElement<? extends Result<?>>) query.distinct();
            }

            return RequeryUtils.unwrap(query.where().exists(criteria));
        }
        return query;
    }
}
