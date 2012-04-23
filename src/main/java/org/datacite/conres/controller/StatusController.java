package org.datacite.conres.controller;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.datacite.conres.service.impl.SearchServiceImpl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;

@Path("/status")
public class StatusController {
    @GET
    public Response get() throws URISyntaxException {
        Client client = Client.create();
        WebResource webResource = client.resource(SearchServiceImpl.SOLR_STATUS_URL);
        ClientResponse cr = webResource.get(ClientResponse.class);
        if (cr.getStatus() == 200)
            return Response.ok("OK").build();
        else
            return Response.serverError().build();
    }
}
