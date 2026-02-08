package de.roamingthings.myservice.greetings.boundary;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
class GreetingsResourceIT {

    @Inject
    @RestClient
    GreetingsResourceClient rut;

    @Inject
    @ConfigProperty(name = "quarkus.rest-client.myservice.url")
    String baseURI;


    @Test
    void hello() {
        var message = this.rut.content();
        assertNotNull(message);
        IO.println("baseURI: %s message: %s".formatted(baseURI, message));
    }

}
