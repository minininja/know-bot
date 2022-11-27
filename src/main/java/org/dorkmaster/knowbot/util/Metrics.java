package org.dorkmaster.knowbot.util;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Metrics {
    protected static final MetricRegistry metrics = new MetricRegistry();
    protected static Map<String, Meter> meters = new ConcurrentHashMap<>();

    static {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(300, TimeUnit.SECONDS);

        String graphiteHost = System.getenv("GRAPHITE");
        if (null != graphiteHost && graphiteHost.trim().length() > 0) {
            System.out.println("Setting up graphite reporter");
            Graphite graphite = new Graphite(new InetSocketAddress(graphiteHost, 2003));
            GraphiteReporter graphiteReporter = GraphiteReporter.forRegistry(metrics)
                    .prefixedWith("knowbot")
                    .convertRatesTo(TimeUnit.SECONDS)
                    .convertDurationsTo(TimeUnit.MILLISECONDS)
                    .build(graphite);
            graphiteReporter.start(15, TimeUnit.SECONDS);
        }
    }

    protected static void counter(String key) {
        metrics.counter(key).inc();;
    }

    protected static void meter(String key) {
        metrics.meter(key).mark();
    }

    public static void inc(String name) {
        counter(name);
    }

    public static void mark(String name) {
        meter(name);
    }
}
