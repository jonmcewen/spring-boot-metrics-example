# spring-boot-metrics-example
This example project uses Dropwizard Metrics to export JVM metrics to Graphite.  

Spring Boot auto-configures the `com.codahale.metrics.MetricRegistry`.  See code comments in `Application.java` for further details.

Requires Graphite server running with UDP enabled.  If Graphite is not on your local machine, replace `localhost` in `Application.java` with the hostname or IP addres of your Graphite server.  

If you want to use TCP instead of UDP, use `com.codahale.metrics.graphite.Graphite` instead of `com.codahale.metrics.graphite.GraphiteUDP`.
