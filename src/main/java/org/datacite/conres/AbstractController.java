package org.datacite.conres;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Variant;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractController {
    protected SearchService service;
    protected String doi;
    protected Map<MediaType,URI> userMedia;
    protected boolean doiRegistered;
    protected String allocatorName;
    protected String datacentreName;
    protected UriInfo uriInfo;

    public AbstractController(String doi, UriInfo uriInfo) {
        this.doi = doi;
        this.uriInfo = uriInfo;
        String doiPrefix = doi.substring(0, doi.indexOf("/"));
        service = SearchServiceFactory.getInstance(doiPrefix);
        doiRegistered = service.isDoiRegistered(doi);
        if (doiRegistered) {
            userMedia = service.getMedia(doi);
            allocatorName = service.getAllocatorName(doi);
            datacentreName = service.getDatacentreName(doi);
        }
    }

    private Metadata buildModel(String xml) {
        String contextPath = uriInfo.getBaseUri().getPath();
        return new Metadata(doi,
                xml,
                userMedia,
                contextPath.substring(0, contextPath.length() - 1),
                allocatorName,
                datacentreName);
    }

    protected List<Variant> allSupportedTypes() {
        List<MediaType> allSupportedTypes = new ArrayList<MediaType>();
        for (MediaType m : userMedia.keySet()) {
            allSupportedTypes.add(m);
        }
        for(Representation c: Representation.values()){
            allSupportedTypes.add(c.asMediaType());
        }

        MediaType[] allSupportedMedia = allSupportedTypes.toArray(new MediaType[allSupportedTypes.size()]);
        return Variant.mediaTypes(allSupportedMedia).add().build();
    }

    protected Response buildResponse(Variant v){
        if (v == null)
            return Response.notAcceptable(allSupportedTypes()).build();

        // is it just redirect...?
        MediaType requestedMedia = v.getMediaType();
        if(userMedia.keySet().contains(requestedMedia)){
            return Response.seeOther(userMedia.get(requestedMedia)).build();
        }

        // ...no, we have to act accordingly
        Representation r;
        if ((r = Representation.valueOf(v)) == null)
            return Response.notAcceptable(allSupportedTypes()).build();

        String xml = service.getXml(doi);
        if (r == Representation.TEXT_HTML || !(xml==null || "".equals(xml))) {
            Metadata model = buildModel(xml);
            Object entity = r.render(model);
            return Response.ok(entity).type(r.asMediaType()).build();
        } else
            return Response.noContent().build();
    }
}
