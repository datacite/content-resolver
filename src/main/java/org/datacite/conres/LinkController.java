package org.datacite.conres;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

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
    public Response get(@Context UriInfo uriInfo){
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

        String contextPath = uriInfo.getBaseUri().getPath();
        Metadata bean = new Metadata(doi,
                xml,
                userMedia,
                contextPath.substring(0, contextPath.length() - 1),
                allocatorName,
                datacentreName);
        return Response.ok(representation.render(bean)).type(requestedMedia).build();
    }
}
