package de.roamingthings.myservice.greetings.boundary;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class GreetingResourceTest {

    @Test
    void helloEndpointReturnsDefaultGreeting() {
        var body = given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .extract().body().asString();

        assertThat(body).isEqualTo("hello, Quarkus on BCE");
    }
}
