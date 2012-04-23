package org.datacite.conres.controller;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;

@Path("/status")
public class StatusController {
    @GET
    public Response get() throws URISyntaxException {
        Client client = Client.create();
        WebResource webResource = client.resource("http://search.datacite.org/status");
        ClientResponse cr = webResource.get(ClientResponse.class);
        if (cr.getStatus() == 200)
            return Response.ok("OK").build();
        else
            return Response.serverError().build();
    }
}
