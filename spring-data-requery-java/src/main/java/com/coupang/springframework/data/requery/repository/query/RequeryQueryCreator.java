package com.coupang.springframework.data.requery.repository.query;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;

import java.util.Iterator;

/**
 * // 참고: arango-spring-data의 DerivedQueryCreator
 * <p>
 * Query creator to create a {@link io.requery.sql.QueryBuilder} from a {@link PartTree}.
 *
 * @author debop
 * @since 18. 6. 7
 */
public class RequeryQueryCreator extends AbstractQueryCreator<String, ConjunctionBuilder> {


    public RequeryQueryCreator(PartTree tree) {
        super(tree);
    }

    public RequeryQueryCreator(PartTree tree, ParameterAccessor parameters) {
        super(tree, parameters);
    }

    @Override
    protected ConjunctionBuilder create(Part part, Iterator<Object> iterator) {
        return null;
    }

    @Override
    protected ConjunctionBuilder and(Part part, ConjunctionBuilder base, Iterator<Object> iterator) {
        return null;
    }

    @Override
    protected ConjunctionBuilder or(ConjunctionBuilder base, ConjunctionBuilder criteria) {
        return null;
    }

    @Override
    protected String complete(ConjunctionBuilder criteria, Sort sort) {
        return null;
    }
}
