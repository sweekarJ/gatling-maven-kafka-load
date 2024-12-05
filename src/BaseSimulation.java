package simulations;


import io.gatling.core.Predef.*;
import io.gatling.http.Predef.*;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.http.HttpDsl.http;

public abstract class BaseSimulation extends Simulation {
    // Common protocol configurations
    protected final HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080") // Change to your API base URL
            .acceptHeader("application/json")
            .userAgentHeader("Gatling/3.13.1");
}
