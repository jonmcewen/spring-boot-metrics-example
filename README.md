# spring-boot-metrics-example
This example project uses Dropwizard Metrics to export JVM metrics to Graphite.  

Spring Boot auto-configures the `com.codahale.metrics.MetricRegistry`.  See code comments in `Application.java` for further details.

Requires Graphite server running with UDP enabled (set `ENABLE_UDP_LISTENER = True` in carbon.conf).

If Graphite is not on your local machine, replace `localhost` in `Application.java` with the hostname or IP address of your Graphite server.  

If you want to use TCP instead of UDP, use `com.codahale.metrics.graphite.Graphite` instead of `com.codahale.metrics.graphite.GraphiteUDP`, as shown here: http://metrics.dropwizard.io/3.1.0/manual/graphite/

When you're ready to try it:
`mvn clean install && java -jar target/metrics-example-0.0.1-SNAPSHOT.jar`
