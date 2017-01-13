package org.datacite.conres.controller;

import com.google.common.cache.CacheStats;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.log4j.Logger;
import org.datacite.conres.Configuration;
import org.datacite.conres.service.impl.SearchServiceImpl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;

@Path("/heartbeat")
public class StatusController {
    private static final Logger log4j = Logger.getLogger(StatusController.class);

    @GET
    public Response get() throws URISyntaxException {
        Client client = Client.create();
        WebResource webResource = client.resource(Configuration.API_STATUS_URL);
        ClientResponse cr = webResource.get(ClientResponse.class);
        if (cr.getStatus() == 200)
            return Response.ok("OK").build();
        else
            return Response.serverError().build();
    }
}
