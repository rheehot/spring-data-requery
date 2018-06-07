package com.coupang.springframework.data.requery.repository.query;

import io.requery.query.Selection;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;

import java.util.Iterator;
import java.util.function.Predicate;

/**
 * com.coupang.springframework.data.requery.repository.query.RequeryQueryCreator
 *
 * @author debop
 * @since 18. 6. 7
 */
public class RequeryQueryCreator extends AbstractQueryCreator<Selection<? extends Object>, Predicate> {

    public RequeryQueryCreator(PartTree tree) {
        super(tree);
    }

    public RequeryQueryCreator(PartTree tree, ParameterAccessor parameters) {
        super(tree, parameters);
    }

    @Override
    protected Predicate create(Part part, Iterator<Object> iterator) {
        return null;
    }

    @Override
    protected Predicate and(Part part, Predicate base, Iterator<Object> iterator) {
        return null;
    }

    @Override
    protected Predicate or(Predicate base, Predicate criteria) {
        return null;
    }

    @Override
    protected Selection<? extends Object> complete(Predicate criteria, Sort sort) {
        return null;
    }
}
