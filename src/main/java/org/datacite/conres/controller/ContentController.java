package org.datacite.conres.controller;

import org.apache.log4j.Logger;

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

    static final Logger log4j = Logger.getLogger(ContentController.class);

    public ContentController(@PathParam("doi")String doi,
                             @Context UriInfo uriInfo,
                             @Context HttpHeaders headers){
        super(doi, uriInfo, headers);
    }

    @GET
    public Response get(@Context Request r) {
        if (model == null)
            return Response.status(404).build();

        Variant v = r.selectVariant(allSupportedTypes());
        log4j.info("GET " + model.getDoi() +
                   " as " + v.getMediaType() +
                   ("".equals(model.getCslStyle()) ? "" : " style:" + model.getCslStyle()) +
                   ("".equals(model.getCslLocale()) ? "" : " locale:" + model.getCslLocale()));
        return buildResponse(v);
    }
}
