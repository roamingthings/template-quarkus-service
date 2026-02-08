package de.roamingthings.myservice.greetings.boundary;

import de.roamingthings.myservice.health.boundary.HealthClient;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class HealthResourceIT {

    @Inject
    @RestClient
    HealthClient rut;

    @Inject
    @ConfigProperty(name = "quarkus.rest-client.myservice.url")
    String baseURI;


    @Test
    void liveness() {
        var response = this.rut.liveness();
        assertNotNull(response);
        IO.println("baseURI: %s status: %s".formatted(baseURI, response.getStatus()));
    }

    @Test
    void readiness() {
        var response = this.rut.readiness();
        assertNotNull(response);
        IO.println("baseURI: %s status: %s".formatted(baseURI, response));
    }

}
