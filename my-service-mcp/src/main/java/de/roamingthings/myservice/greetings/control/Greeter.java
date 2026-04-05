package de.roamingthings.myservice.greetings.control;

import static java.lang.System.Logger.Level.*;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class Greeter {

    static final System.Logger LOGGER = System.getLogger(Greeter.class.getName());

    @Inject
    @ConfigProperty(defaultValue = "hello, Quarkus MCP on BCE", name = "message")
    String message;

    public String greetings() {
        LOGGER.log(INFO, "returning: " + this.message);
        return this.message;
    }

    public String greetings(String name) {
        var personalGreeting = "Hello, " + name + "!";
        LOGGER.log(INFO, "greeting: " + personalGreeting);
        return personalGreeting;
    }
}
