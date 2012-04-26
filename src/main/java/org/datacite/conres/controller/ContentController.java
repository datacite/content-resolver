package org.datacite.conres.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.*;

/**
 * Deals with content negotiation requests
 *
 * @see LinkController
 */
@Path("/{doi: 10\\..*}")
public class ContentController extends AbstractController{

    public ContentController(@PathParam("doi")String doi,
                             @Context UriInfo uriInfo,
                             @Context HttpHeaders headers){
        super(doi, uriInfo, headers);
    }

    @GET
    public Response get(@Context Request r) {
        if (model == null)
            return Response.status(404).build();

        return buildResponse(r.selectVariant(allSupportedTypes()));
    }
}
