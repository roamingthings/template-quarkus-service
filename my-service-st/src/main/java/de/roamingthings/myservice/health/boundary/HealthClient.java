package de.roamingthings.myservice.health.boundary;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/q/health")
@RegisterRestClient(configKey = "myservice")
public interface HealthClient {

    @GET
    @Path("/live")
    Response liveness();

    @GET
    @Path("/ready")
    Response readiness();
}
