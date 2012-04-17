package org.datacite.conres.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.*;

/**
 * Deals with link requests
 *
 * @see ContentController
 */
@Path("/{type}/{subtype}/{doi: 10\\..*}")
public class LinkController extends AbstractController {
    private MediaType requestedMedia;

    public LinkController(@PathParam("type")String type,
                          @PathParam("subtype")String subtype,
                          @PathParam("doi")String doi,
                          @Context UriInfo uriInfo){
        super(doi, uriInfo);
        requestedMedia = new MediaType(type, subtype);
    }

    @GET
    public Response get() {
        if (!doiRegistered)
            return Response.status(404).build();

        return buildResponse(new Variant(requestedMedia, null, null));
    }
}
