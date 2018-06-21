package com.coupang.springframework.data.requery.utils;

import com.coupang.springframework.data.requery.core.EntityState;
import com.coupang.springframework.data.requery.domain.AbstractDomainTest;
import com.coupang.springframework.data.requery.domain.basic.BasicGroup;
import com.coupang.springframework.data.requery.domain.basic.BasicUser;
import io.requery.meta.Attribute;
import io.requery.meta.EntityModel;
import io.requery.meta.Type;
import io.requery.query.*;
import io.requery.query.element.QueryElement;
import io.requery.sql.EntityContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class RequeryUtilsTest extends AbstractDomainTest {

    @Test
    public void retrieveKeyPropertyFromBasicUser() {
        NamedExpression<?> keyExpr = RequeryUtils.getKeyExpression(BasicUser.class);

        assertThat(keyExpr).isNotNull();
        assertThat(keyExpr.getName()).isEqualTo("id");
        assertThat(keyExpr.getClassType()).isEqualTo(Long.class);
    }

    @Test
    public void retrieveKeyPropertyFromBasicGroup() {
        NamedExpression<?> keyExpr = RequeryUtils.getKeyExpression(BasicGroup.class);

        assertThat(keyExpr).isNotNull();
        assertThat(keyExpr.getName()).isEqualTo("id");
        assertThat(keyExpr.getClassType()).isEqualTo(Integer.class);
    }

    @Test
    public void getEntityContextFromEntityDataStore() {

        EntityContext entityContext = RequeryUtils.getEntityContext(dataStore);
        assertThat(entityContext).isNotNull();
    }

    @Test
    public void getEntityModelFromEntityDataStore() {

        EntityModel entityModel = RequeryUtils.getEntityModel(dataStore);
        assertThat(entityModel).isNotNull();
        assertThat(entityModel.getName()).isEqualTo("default");
    }

    @Test
    public void retrieveEntityModel() {
        EntityModel entityModel = RequeryUtils.getEntityModel(requeryTemplate.getDataStore());

        assertThat(entityModel).isNotNull();

        assertThat(entityModel.getName()).isEqualTo("default");
        assertThat(entityModel.containsTypeOf(BasicUser.class)).isTrue();

        for (Type type : entityModel.getTypes()) {
            log.debug("Entity class={}", type.getClassType());
        }
    }

    @Test
    public void retrieveEntityClasses() {
        List<Class<?>> classes = RequeryUtils.getEntityClasses(requeryTemplate.getDataStore());

        assertThat(classes.contains(BasicUser.class)).isTrue();
        assertThat(classes.contains(EntityState.class)).isFalse();
    }

    @Test
    public void getEntityTypesFromEntityDataStore() {

        Set<Type<?>> types = RequeryUtils.getEntityTypes(dataStore);
        assertThat(types).isNotEmpty().contains(BasicUser.$TYPE);
    }

    @Test
    public void getEntityClassesFromEntityDataStore() {

        List<Class<?>> classes = RequeryUtils.getEntityClasses(dataStore);
        assertThat(classes).isNotEmpty().contains(BasicUser.class);
    }

    @Test
    public void getTypeFromEntityDataStore() {

        Type<BasicUser> basicUserType = RequeryUtils.getType(dataStore, BasicUser.class);
        assertThat(basicUserType).isEqualTo(BasicUser.$TYPE);
    }

    @Test
    public void getIdAttributeFromDomainClass() {

        Set<? extends Attribute<BasicUser, ?>> keyAttrs = RequeryUtils.getKeyAttributes(dataStore, BasicUser.class);
        assertThat(keyAttrs).hasSize(1);

        Attribute<BasicUser, ?> idAttr = keyAttrs.iterator().next();
        assertThat(idAttr.getClassType()).isEqualTo(Long.class);
    }

    @Test
    public void getSingleIdAttributeFromDomainClass() {

        Attribute<BasicUser, ?> keyAttr = RequeryUtils.getSingleKeyAttribute(dataStore, BasicUser.class);
        assertThat(keyAttr).isNotNull();
        assertThat(keyAttr.getClassType()).isEqualTo(Long.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void applyPageableToQuery() {

        Pageable pageable = PageRequest.of(1, 3, Sort.by("name", "email"));

        Return<? extends Result<BasicUser>> query = (Return<? extends Result<BasicUser>>)
            RequeryUtils.applyPageable(BasicUser.class,
                                       (QueryElement<? extends Result<BasicUser>>) dataStore.select(BasicUser.class),
                                       pageable);

        assertThat(query).isInstanceOf(QueryElement.class);
        assertThat(query).isInstanceOf(Limit.class);
        assertThat(query).isInstanceOf(Offset.class);

        QueryElement<? extends Result<BasicUser>> queryElement = (QueryElement<? extends Result<BasicUser>>) query;
        assertThat(queryElement).isNotNull();
        assertThat(queryElement.getOrderByExpressions()).isNotEmpty();

        for (Expression<?> expr : queryElement.getOrderByExpressions()) {
            log.debug("order by {}", expr);
        }
    }

}
