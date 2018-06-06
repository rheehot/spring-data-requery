package com.coupang.springframework.data.requery.mapping;

import com.coupang.springframework.data.requery.provider.ProxyIdAccessor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mapping.IdentifierAccessor;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.model.BasicPersistentEntity;
import org.springframework.data.mapping.model.IdPropertyIdentifierAccessor;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;

/**
 * com.coupang.springframework.data.requery.mapping.RequeryPersistentEntityImpl
 *
 * @author debop
 * @since 18. 6. 7
 */
public class RequeryPersistentEntityImpl<T>
    extends BasicPersistentEntity<T, RequeryPersistentProperty> implements RequeryPersistentEntity<T> {

    private static final String INVALID_VERSION_ANNOTATION =
        "%s is annotated with " + org.springframework.data.annotation.Version.class.getName() +
        " but needs to use " + javax.persistence.Version.class.getName() + " to trigger optimistic locking correctly!";

    private final ProxyIdAccessor proxyIdAccessor;

    public RequeryPersistentEntityImpl(TypeInformation<T> information,
                                       ProxyIdAccessor proxyIdAccessor) {
        super(information, null);
        Assert.notNull(proxyIdAccessor, "ProxyIdAccessor must not be null!");
        this.proxyIdAccessor = proxyIdAccessor;
    }

    @Override
    protected RequeryPersistentProperty returnPropertyIfBetterIdPropertyCandidateOrNull(RequeryPersistentProperty property) {
        return property.isIdProperty() ? property : null;
    }

    @NotNull
    @Override
    public IdentifierAccessor getIdentifierAccessor(@NotNull Object bean) {
        return new RequeryProxyAwareIdentifierAccessor(this, bean, proxyIdAccessor);
    }

    private static class RequeryProxyAwareIdentifierAccessor extends IdPropertyIdentifierAccessor {

        private final Object bean;
        private final ProxyIdAccessor proxyIdAccessor;

        /**
         * Creates a new {@link IdPropertyIdentifierAccessor} for the given {@link PersistentEntity} and
         * {@link ConvertingPropertyAccessor}.
         *
         * @param entity must not be {@literal null}.
         * @param target must not be {@literal null}.
         */
        public RequeryProxyAwareIdentifierAccessor(PersistentEntity<?, ?> entity, Object bean, ProxyIdAccessor proxyIdAccessor) {
            super(entity, bean);
            Assert.notNull(proxyIdAccessor, "ProxyIdAccessor must not be null!");

            this.proxyIdAccessor = proxyIdAccessor;
            this.bean = bean;
        }

        @Override
        public Object getIdentifier() {
            return proxyIdAccessor.shouldUseAccessorFor(bean)
                   ? proxyIdAccessor.getIdentifierFrom(bean)
                   : super.getIdentifier();
        }
    }
}
