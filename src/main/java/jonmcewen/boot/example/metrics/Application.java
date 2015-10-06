package jonmcewen.boot.example.metrics;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricSet;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.graphite.GraphiteSender;
import com.codahale.metrics.graphite.GraphiteUDP;
import com.codahale.metrics.jvm.FileDescriptorRatioGauge;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;

/**
 * @author jon.mcewen
 * 
 *         A very simple Spring Boot application, that does nothing useful, but
 *         reports metrics to Graphite via dropwizard-metrics. Has standard
 *         actuator endpoints such as /beans.
 *
 */
@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	@Autowired
	private MetricRegistry metricRegistry;

	/**
	 * @param args
	 *            no command line args required
	 */
	public static void main(String[] args) {
		log.info(" *** STARTING EXAMPLE APPLICATION ***");
		SpringApplication.run(Application.class, args);

	}

	/*
	 * Create reporter bean and tell Spring to call stop() when shutting down
	 */
	@Bean(destroyMethod = "stop")
	GraphiteReporter graphiteReporter() {
		// add some JVM metrics (wrap in MetricSet to add better key prefixes)
		MetricSet jvmMetrics = new MetricSet() {

			@Override
			public Map<String, com.codahale.metrics.Metric> getMetrics() {

				Map<String, com.codahale.metrics.Metric> metrics = new HashMap<String, com.codahale.metrics.Metric>();
				metrics.put("gc", new GarbageCollectorMetricSet());
				metrics.put("file-descriptors", new FileDescriptorRatioGauge());
				metrics.put("memory-usage", new MemoryUsageGaugeSet());
				metrics.put("threads", new ThreadStatesGaugeSet());
				return metrics;
			}
		};
		metricRegistry.registerAll(jvmMetrics);

		// create and start reporter
		final GraphiteSender graphite = new GraphiteUDP(new InetSocketAddress("localhost", 2003));
		final GraphiteReporter reporter = GraphiteReporter.forRegistry(metricRegistry).prefixedWith("spring-boot").convertRatesTo(TimeUnit.SECONDS)
				.convertDurationsTo(TimeUnit.MILLISECONDS).filter(MetricFilter.ALL).build(graphite);
		reporter.start(1, TimeUnit.MINUTES);
		return reporter;
	}

}
