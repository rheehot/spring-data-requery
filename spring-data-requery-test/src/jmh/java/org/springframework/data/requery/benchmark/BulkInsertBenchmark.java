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

/*
Benchmark                                                           Mode  Cnt         Score          Error   Units
BulkInsertBenchmark.ins10_000                                       avgt   10      1525.456 ±      386.770   ms/op
BulkInsertBenchmark.ins10_000:·gc.alloc.rate                        avgt   10       395.063 ±       91.665  MB/sec
BulkInsertBenchmark.ins10_000:·gc.alloc.rate.norm                   avgt   10  60251213.259 ±   488176.745    B/op
BulkInsertBenchmark.ins10_000:·gc.churn.PS_Eden_Space               avgt   10       398.023 ±      109.856  MB/sec
BulkInsertBenchmark.ins10_000:·gc.churn.PS_Eden_Space.norm          avgt   10  60556233.272 ±  9071246.112    B/op
BulkInsertBenchmark.ins10_000:·gc.churn.PS_Old_Gen                  avgt   10         0.002 ±        0.011  MB/sec
BulkInsertBenchmark.ins10_000:·gc.churn.PS_Old_Gen.norm             avgt   10       365.394 ±     1746.918    B/op
BulkInsertBenchmark.ins10_000:·gc.churn.PS_Survivor_Space           avgt   10         9.112 ±       20.200  MB/sec
BulkInsertBenchmark.ins10_000:·gc.churn.PS_Survivor_Space.norm      avgt   10   1711198.994 ±  4073607.020    B/op
BulkInsertBenchmark.ins10_000:·gc.count                             avgt   10        33.000                 counts
BulkInsertBenchmark.ins10_000:·gc.time                              avgt   10      8357.000                     ms
BulkInsertBenchmark.ins1_000                                        avgt   10       151.903 ±       32.374   ms/op
BulkInsertBenchmark.ins1_000:·gc.alloc.rate                         avgt   10       268.210 ±       93.248  MB/sec
BulkInsertBenchmark.ins1_000:·gc.alloc.rate.norm                    avgt   10   5521697.363 ±   232271.433    B/op
BulkInsertBenchmark.ins1_000:·gc.churn.Compressed_Class_Space       avgt   10         0.002 ±        0.010  MB/sec
BulkInsertBenchmark.ins1_000:·gc.churn.Compressed_Class_Space.norm  avgt   10        95.883 ±      458.408    B/op
BulkInsertBenchmark.ins1_000:·gc.churn.Metaspace                    avgt   10         0.005 ±        0.024  MB/sec
BulkInsertBenchmark.ins1_000:·gc.churn.Metaspace.norm               avgt   10       237.912 ±     1137.437    B/op
BulkInsertBenchmark.ins1_000:·gc.churn.PS_Eden_Space                avgt   10       204.750 ±      354.460  MB/sec
BulkInsertBenchmark.ins1_000:·gc.churn.PS_Eden_Space.norm           avgt   10   5502832.405 ± 10679130.092    B/op
BulkInsertBenchmark.ins1_000:·gc.churn.PS_Survivor_Space            avgt   10         1.879 ±        8.477  MB/sec
BulkInsertBenchmark.ins1_000:·gc.churn.PS_Survivor_Space.norm       avgt   10     84884.790 ±   394692.141    B/op
BulkInsertBenchmark.ins1_000:·gc.count                              avgt   10         6.000                 counts
BulkInsertBenchmark.ins1_000:·gc.time                               avgt   10      1000.000                     ms
BulkInsertBenchmark.ins5_000                                        avgt   10       377.402 ±      161.913   ms/op
BulkInsertBenchmark.ins5_000:·gc.alloc.rate                         avgt   10       363.065 ±       55.305  MB/sec
BulkInsertBenchmark.ins5_000:·gc.alloc.rate.norm                    avgt   10  29699623.726 ±   600784.517    B/op
BulkInsertBenchmark.ins5_000:·gc.churn.Compressed_Class_Space       avgt   10         0.001 ±        0.007  MB/sec
BulkInsertBenchmark.ins5_000:·gc.churn.Compressed_Class_Space.norm  avgt   10       134.034 ±      640.806    B/op
BulkInsertBenchmark.ins5_000:·gc.churn.Metaspace                    avgt   10         0.004 ±        0.017  MB/sec
BulkInsertBenchmark.ins5_000:·gc.churn.Metaspace.norm               avgt   10       333.531 ±     1594.585    B/op
BulkInsertBenchmark.ins5_000:·gc.churn.PS_Eden_Space                avgt   10       351.484 ±      208.337  MB/sec
BulkInsertBenchmark.ins5_000:·gc.churn.PS_Eden_Space.norm           avgt   10  28883336.474 ± 16126785.906    B/op
BulkInsertBenchmark.ins5_000:·gc.churn.PS_Survivor_Space            avgt   10         4.547 ±       11.020  MB/sec
BulkInsertBenchmark.ins5_000:·gc.churn.PS_Survivor_Space.norm       avgt   10    411173.074 ±   984688.852    B/op
BulkInsertBenchmark.ins5_000:·gc.count                              avgt   10        11.000                 counts
BulkInsertBenchmark.ins5_000:·gc.time                               avgt   10      2183.000                     ms
BulkInsertBenchmark.insTen                                          avgt   10         2.339 ±        0.210   ms/op
BulkInsertBenchmark.insTen:·gc.alloc.rate                           avgt   10       231.141 ±       18.701  MB/sec
BulkInsertBenchmark.insTen:·gc.alloc.rate.norm                      avgt   10     70523.146 ±     1108.136    B/op
BulkInsertBenchmark.insTen:·gc.churn.PS_Eden_Space                  avgt   10       236.402 ±      332.576  MB/sec
BulkInsertBenchmark.insTen:·gc.churn.PS_Eden_Space.norm             avgt   10     74921.056 ±   112545.486    B/op
BulkInsertBenchmark.insTen:·gc.churn.PS_Survivor_Space              avgt   10         0.155 ±        0.743  MB/sec
BulkInsertBenchmark.insTen:·gc.churn.PS_Survivor_Space.norm         avgt   10        48.531 ±      232.022    B/op
BulkInsertBenchmark.insTen:·gc.count                                avgt   10         7.000                 counts
BulkInsertBenchmark.insTen:·gc.time                                 avgt   10       166.000                     ms

 */
@BenchmarkMode(Mode.AverageTime)
@Threads(Threads.MAX)
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 10)
@Measurement(iterations = 10)
@Fork(1)
public class BulkInsertBenchmark {

    private static Random rnd = new Random(System.currentTimeMillis());

    private static FullLog randomFullLog() {
        FullLog fullLog = new FullLog();
        fullLog.setCreateAt(new Date());
        fullLog.setSystemId("SystemId:" + rnd.nextInt(1000));
        fullLog.setSystemName("SystemName:" + rnd.nextInt(1000));
        fullLog.setLogLevel(rnd.nextInt(5));
        fullLog.setThreadName("main-" + rnd.nextInt(16));
        fullLog.setLogMessage("동해물과 백두산이 마르고 닳도록, 동해물과 백두산이 마르고 닳도록, 동해물과 백두산이 마르고 닳도록");

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
        dataStore = RequerySetupUtils.dataStore;
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
    public void ins5_000() {
        insertLogs(5_000);
    }

//    @Benchmark
//    public void ins10_000() {
//        insertLogs(10_000);
//    }

//    @Benchmark
//    public void ins50_000() {
//        insertLogs(50_000);
//    }

    private void insertLogs(int count) {
        List<FullLog> fullLogs = randomFullLogs(count);
        dataStore.insert(fullLogs);
    }

}
