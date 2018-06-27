package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import io.requery.query.element.QueryElement;
import io.requery.query.function.Count;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.ReturnedType;
import org.springframework.data.repository.query.parser.PartTree;

import static com.coupang.springframework.data.requery.utils.RequeryUtils.unwrap;

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

    @Override
    protected QueryElement<?> createQueryElement(ReturnedType type) {
        return unwrap(getOperations().select(Count.count(type.getDomainType())));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected QueryElement<?> complete(QueryElement<?> criteria,
                                       Sort sort,
                                       QueryElement<?> root) {

        QueryElement<?> query = unwrap(getOperations().select(Count.count(getDomainClass())));

        if (criteria != null) {
            if (criteria.isDistinct()) {
                query = unwrap(query.distinct());
            }

            return unwrap(query.where().exists(criteria));
        }
        return query;
    }
}
