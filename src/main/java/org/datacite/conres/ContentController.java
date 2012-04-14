package org.datacite.conres;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

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
    public Response get(@Context Request request) {
        if (!doiRegistered)
            return Response.status(404).build();

        List<Variant> allSupportedType = allSupportedTypes();
        Variant bestMatch = request.selectVariant(allSupportedType);
        if ( bestMatch == null) {
            return Response.notAcceptable(allSupportedType).build();
        } else {
            MediaType requestedMedia = bestMatch.getMediaType();

            if (userMedia.keySet().contains(requestedMedia))
                return Response.seeOther(userMedia.get(requestedMedia)).build();

            Representation representation = Representation.valueOf(requestedMedia);

            // TODO is this the right thing to do?
            String xml = service.getXml(doi);
            if (xml==null || "".equals(xml))
                return Response.noContent().build();

            Metadata model = buildModel(xml);
            Object entity = representation.render(model);
            return Response.ok(entity).type(requestedMedia).build();
        }
    }
}
