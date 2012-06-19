package org.datacite.conres.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Controller to show welcome page when no DOI is present in the path
 */
@Path("/")
public class RootController {

    @GET
    public Response get() throws URISyntaxException {
        return Response.seeOther(new URI("/static/index.html")).build();
    }
}
