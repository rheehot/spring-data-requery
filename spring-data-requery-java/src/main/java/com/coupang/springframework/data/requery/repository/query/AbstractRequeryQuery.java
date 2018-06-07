package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.utils.EntityDataStoreUtils;
import com.coupang.springframework.data.requery.utils.RequeryMetamodel;
import io.requery.sql.EntityDataStore;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.repository.query.RepositoryQuery;

import javax.validation.constraints.NotNull;

/**
 * com.coupang.springframework.data.requery.repository.query.AbstractRequeryQuery
 *
 * @author debop
 * @since 18. 6. 7
 */
@Slf4j
@Getter
public abstract class AbstractRequeryQuery implements RepositoryQuery {

    private final RequeryQueryMethod method;
    private final EntityDataStore entityDataStore;
    private final RequeryMetamodel metamodel;


    public AbstractRequeryQuery(@NotNull RequeryQueryMethod method, @NotNull EntityDataStore entityDataStore) {
        this.method = method;
        this.entityDataStore = entityDataStore;
        this.metamodel = new RequeryMetamodel(EntityDataStoreUtils.getEntityModel(entityDataStore));
    }

    public Object execute(Object[] parameters) {
        return doExecute(getExecution(), parameters);
    }

    private Object doExecute(@NotNull RequeryQueryExecution execution, Object[] values) {
        throw new NotImplementedException("구현 중");
    }

    protected RequeryQueryExecution getExecution() {
        throw new NotImplementedException("구현 중");
    }
}
