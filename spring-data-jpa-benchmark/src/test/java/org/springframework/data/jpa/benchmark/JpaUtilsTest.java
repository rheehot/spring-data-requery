package org.springframework.data.jpa.benchmark;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.benchmark.model.FullLog;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Date;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * JpaUtilsTest
 *
 * @author debop@coupang.com
 */
public class JpaUtilsTest {

    private static EntityManagerFactory emf = JpaUtils.getEntityManagerFactory();
    private static PlatformTransactionManager tm = new JpaTransactionManager(emf);
    private static TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();

    private static Random rnd = new Random(System.currentTimeMillis());

    private static FullLog randomFullLog() {
        FullLog fullLog = new FullLog();
        fullLog.setCreateAt(new Date());
        fullLog.setSystemId("SystemId:" + rnd.nextInt(1000));
        fullLog.setSystemName("SystemId:" + rnd.nextInt(1000));
        fullLog.setLogLevel(rnd.nextInt(5));
        fullLog.setThreadName("main-" + rnd.nextInt(16));

        return fullLog;
    }

    private EntityManager em;
    private TransactionStatus ts;

    @Before
    public void setup() {
        ts = tm.getTransaction(transactionDefinition);
        em = emf.createEntityManager();
        em.joinTransaction();
    }

    @After
    public void tearDown() {
        tm.commit(ts);
        if (em != null) {
            em.close();
        }
    }

    @Test
    public void createEntityManagerFactory() {

        assertThat(emf).isNotNull();

        EntityManager em = emf.createEntityManager();
        assertThat(em).isNotNull();
    }

    @Test
    public void insertFullLogWithEntityManager() {

        FullLog fullLog = randomFullLog();

        em.persist(fullLog);
        em.flush();

    }
}
