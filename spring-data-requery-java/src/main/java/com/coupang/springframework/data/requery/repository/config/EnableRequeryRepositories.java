package com.coupang.springframework.data.requery.repository.config;

import com.coupang.springframework.data.requery.repository.support.RequeryRepositoryFactoryBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.repository.config.DefaultRepositoryBaseClass;

import java.lang.annotation.*;

/**
 * EnableRequeryRepositories
 *
 * @author debop@coupang.com
 * @since 18. 6. 4
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(RequeryRepositoriesRegistar.class)
public @interface EnableRequeryRepositories {

    String[] value() default {};

    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};

    ComponentScan.Filter[] includeFilters() default {};

    ComponentScan.Filter[] excludeFilters() default {};

    String repositoryImplementationPostfix() default "Impl";

    String namedQueriesLocation() default "";

    Class<?> repositoryFactoryBeanClass() default RequeryRepositoryFactoryBean.class;

    Class<?> repositoryBaseClass() default DefaultRepositoryBaseClass.class;

    String transactionManagerRef() default "transactionManager";

    boolean enableDefaultTransactions() default true;
}
