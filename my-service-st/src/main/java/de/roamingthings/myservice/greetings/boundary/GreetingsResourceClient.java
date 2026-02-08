package de.roamingthings.myservice.greetings.boundary;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("hello")
@RegisterRestClient(configKey = "myservice")
public interface GreetingsResourceClient {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    String content();

}
