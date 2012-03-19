package org.datacite.conres;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
                          @PathParam("doi")String doi){
        super(doi);
        requestedMedia = new MediaType(type, subtype);
    }

    @GET
    public Response get(){
        if (!doiRegistered)
            return Response.status(404).build();

        if(userMedia.keySet().contains(requestedMedia)){
            return Response.seeOther(userMedia.get(requestedMedia)).build();
        }

        Representation representation = Representation.valueOf(requestedMedia);
        if (representation == null)
            return Response.notAcceptable(allSupportedTypes()).build();

        // TODO is this the right thing to do?
        String xml = service.getXml(doi);
        if (xml==null || "".equals(xml))
            return Response.noContent().build();

        Metadata bean = new Metadata(doi, xml, userMedia);
        return Response.ok(representation.render(bean)).type(requestedMedia).build();
    }
}
