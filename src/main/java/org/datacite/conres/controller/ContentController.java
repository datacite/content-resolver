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
@Path("/{doi: 10\\.[0-9]{4}/.+}")
public class ContentController extends BaseController {
    public ContentController(@PathParam("doi")String doi,
                             @Context HttpHeaders headers){
        super(doi, headers);
        log4j.debug("New request for " + doi + " as " + acceptHeader);
    }

    @GET
    public Response get(@Context Request r, @PathParam("doi")String doi) {
        if (model == null) {
            log4j.info("No content for " + doi);
            return Response.status(404).build();
        }

        Variant v = r.selectVariant(allSupportedTypes());
        if (v == null) {
            log4j.info("Not acceptable: " +  doi + " as " + acceptHeader);
            return Response.notAcceptable(allSupportedTypes()).build();
        }
        return buildResponse(v);
    }
}
