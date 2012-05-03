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
public class LinkController extends BaseController {
    private MediaType requestedMedia;

    public LinkController(@PathParam("type")String type,
                          @PathParam("subtype")String subtype,
                          @PathParam("doi")String doi,
                          @Context UriInfo uriInfo,
                          @Context HttpHeaders headers){
        super(doi, uriInfo, headers);
        requestedMedia = new MediaType(type, subtype);
        log4j.debug("New request for " + uriInfo.getPath());
    }

    @GET
    public Response get(@PathParam("doi")String doi) {
        if (model == null){
            log4j.info("No content for " + doi);
            return Response.status(404).build();
        }

        return buildResponse(new Variant(requestedMedia, null, null));
    }
}
