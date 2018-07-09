package org.springframework.data.requery.benchmark;

import io.requery.sql.EntityDataStore;
import org.openjdk.jmh.annotations.*;
import org.springframework.data.requery.benchmark.model.FullLog;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * BulkInsertBenchmark
 *
 * @author debop@coupang.com
 */

@BenchmarkMode(Mode.AverageTime)
@Threads(Threads.MAX)
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
public class BulkInsertBenchmark {

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

    private static List<FullLog> randomFullLogs(int count) {
        return IntStream
            .range(0, count)
            .mapToObj(it -> randomFullLog())
            .collect(Collectors.toList());
    }

    private EntityDataStore<Object> dataStore;

    @Setup
    public void setup() {
        dataStore = RequerySetupUtils.getDataStore();
    }


    @Benchmark
    public void insTen() {
        insertLogs(10);
    }

    @Benchmark
    public void ins1_000() {
        insertLogs(1_000);
    }

    @Benchmark
    public void ins10_000() {
        insertLogs(10_000);
    }

//    @Benchmark
//    public void ins50_000() {
//        insertLogs(50_000);
//    }

    private void insertLogs(int count) {
        List<FullLog> fullLogs = randomFullLogs(count);
        dataStore.insert(fullLogs);
    }

}
