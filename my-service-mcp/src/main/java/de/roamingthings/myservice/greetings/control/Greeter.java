package de.roamingthings.myservice.greetings.control;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class Greeter {

    static final Logger LOGGER = LoggerFactory.getLogger(Greeter.class);

    @Inject
    @ConfigProperty(defaultValue = "hello, Quarkus MCP on BCE", name = "message")
    String message;

    public String greetings() {
        LOGGER.info("returning: {}", this.message);
        return this.message;
    }

    public String greetings(String name) {
        var personalGreeting = "Hello, " + name + "!";
        LOGGER.info("greeting: {}", personalGreeting);
        return personalGreeting;
    }
}
