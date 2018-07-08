package org.springframework.data.requery.repository.support;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.requery.configs.RequeryTestConfiguration;
import org.springframework.data.requery.domain.RandomData;
import org.springframework.data.requery.repository.config.EnableRequeryRepositories;
import org.springframework.data.requery.repository.sample.basic.BasicUserRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@ContextConfiguration
public class TransactionalRepositoryTest {

    @Configuration
    @EnableRequeryRepositories(basePackageClasses = { BasicUserRepository.class })
    static class TestConfiguration extends RequeryTestConfiguration {

        @Override
        public PlatformTransactionManager transactionManager() {
            return new DelegatingTransactionManager(super.transactionManager());
        }
    }

    @Autowired BasicUserRepository repository;
    @Autowired DelegatingTransactionManager transactionManager;

    @Before
    public void setup() {
        transactionManager.resetCount();
    }

    @After
    public void tearDown() {
        repository.deleteAll();
    }

    @Test
    public void simpleManipulatingOperation() throws Exception {
        repository.upsert(RandomData.randomUser());
        assertThat(transactionManager.getTransactionRequests()).isEqualTo(1);
    }

    @Test
    public void unannotatedFinder() {
        repository.findByEmail("foo@bar.kr");
        assertThat(transactionManager.getTransactionRequests()).isEqualTo(1);
    }

    @Test
    public void rawQueryString() {
        repository.findByAnnotatedQuery("foo@bar.kr");
        assertThat(transactionManager.getTransactionRequests()).isEqualTo(1);
    }

    @Getter
    @Slf4j
    public static class DelegatingTransactionManager implements PlatformTransactionManager {

        private final PlatformTransactionManager txManager;
        private int transactionRequests;
        private TransactionDefinition definition;

        public DelegatingTransactionManager(PlatformTransactionManager txManager) {
            this.txManager = txManager;
        }

        @Override
        public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
            this.transactionRequests++;
            this.definition = definition;

            log.info("Get transaction. transactionRequests={}, definition={}", transactionRequests, definition);

            return txManager.getTransaction(definition);
        }

        @Override
        public void commit(TransactionStatus status) throws TransactionException {
            log.info("Commit transaction. status={}", status);
            txManager.commit(status);
        }

        @Override
        public void rollback(TransactionStatus status) throws TransactionException {
            log.info("Rollback transaction. status={}", status);
            txManager.rollback(status);
        }

        public void resetCount() {
            log.info("Reset transaction request.");
            this.transactionRequests = 0;
            this.definition = null;
        }
    }
}
