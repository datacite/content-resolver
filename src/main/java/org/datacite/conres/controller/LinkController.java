package org.datacite.conres.controller;

import org.apache.log4j.Logger;

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

    static final Logger log4j = Logger.getLogger(LinkController.class);


    public LinkController(@PathParam("type")String type,
                          @PathParam("subtype")String subtype,
                          @PathParam("doi")String doi,
                          @Context UriInfo uriInfo,
                          @Context HttpHeaders headers){
        super(doi, uriInfo, headers);
        requestedMedia = new MediaType(type, subtype);
    }

    @GET
    public Response get() {
        if (model == null)
            return Response.status(404).build();

        log4j.info("GET " + model.getDoi() + " as " + requestedMedia);
        return buildResponse(new Variant(requestedMedia, null, null));
    }
}
