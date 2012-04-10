package org.datacite.conres;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

@Path("/")
public class RootController {

    @GET
    public Response get() throws URISyntaxException {
        return Response.seeOther(new URI("/static/index.html")).build();
    }
}
