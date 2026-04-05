package de.roamingthings.myservice.greetings.boundary;

import static org.assertj.core.api.Assertions.assertThat;

import io.quarkiverse.mcp.server.test.McpAssured;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Map;
import org.junit.jupiter.api.Test;

@QuarkusTest
class GreetingToolsTest {

    @Test
    void listTools() {
        try (var client = McpAssured.newConnectedStreamableClient()) {
            client.when()
                    .toolsList(page -> {
                        assertThat(page.size()).isEqualTo(2);
                        assertThat(page.findByName("greet")).isNotNull();
                        assertThat(page.findByName("greetByName")).isNotNull();
                    })
                    .thenAssertResults();
        }
    }

    @Test
    void greetReturnsDefaultMessage() {
        try (var client = McpAssured.newConnectedStreamableClient()) {
            client.when()
                    .toolsCall("greet", Map.of(), response -> {
                        assertThat(response.isError()).isFalse();
                        var text = response.firstContent().asText().text();
                        assertThat(text).isEqualTo("hello, Quarkus MCP on BCE");
                    })
                    .thenAssertResults();
        }
    }

    @Test
    void greetByNameReturnsPersonalGreeting() {
        try (var client = McpAssured.newConnectedStreamableClient()) {
            client.when()
                    .toolsCall("greetByName", Map.of("name", "Duke"), response -> {
                        assertThat(response.isError()).isFalse();
                        var text = response.firstContent().asText().text();
                        assertThat(text).isEqualTo("Hello, Duke!");
                    })
                    .thenAssertResults();
        }
    }
}
