package org.datacite.conres.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Deals with content negotiation requests
 *
 * @see LinkController
 */
@Path("/{doi: 10\\..*}")
public class ContentController extends AbstractController{

    public ContentController(@PathParam("doi")String doi, @Context UriInfo uriInfo){
        super(doi, uriInfo);
    }

    @GET
    public Response get(@Context Request r) {
        if (!doiRegistered)
            return Response.status(404).build();

        return buildResponse(r.selectVariant(allSupportedTypes()));
    }
}
