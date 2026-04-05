package de.roamingthings.myservice.greetings.boundary;

import de.roamingthings.myservice.greetings.control.Greeter;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingTools {

    Greeter greeter;

    public GreetingTools(Greeter greeter) {
        this.greeter = greeter;
    }

    @Tool(description = "Returns a greeting message.")
    String greet() {
        return this.greeter.greetings();
    }

    @Tool(description = "Greets a person by name.")
    String greetByName(@ToolArg(description = "Name of the person to greet") String name) {
        return this.greeter.greetings(name);
    }
}
