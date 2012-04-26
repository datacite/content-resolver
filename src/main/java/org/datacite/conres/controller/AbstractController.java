package org.datacite.conres.controller;

import org.datacite.conres.model.Metadata;
import org.datacite.conres.service.SearchService;
import org.datacite.conres.service.SearchServiceFactory;
import org.datacite.conres.view.Representation;

import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractController {
    private List<Variant> allVariants;
    protected Metadata model;

    public AbstractController(String doi, UriInfo uriInfo, HttpHeaders httpHeaders) {
        List<String> acceptList = httpHeaders.getRequestHeader("Accept");
        String acceptHeader;
        if (acceptList.size() > 0)
            acceptHeader = acceptList.get(0);
        else
            acceptHeader = null;
        String doiPrefix = doi.substring(0, doi.indexOf("/"));
        SearchService service = SearchServiceFactory.getInstance(doiPrefix);
        model = service.getMetadata(doi, uriInfo.getBaseUri().getPath(), acceptHeader);
    }

    protected List<Variant> allSupportedTypes() {
        if (allVariants != null)
            return allVariants;

        List<MediaType> allSupportedTypes = new ArrayList<MediaType>();
        for (MediaType m : model.getUserMedia().keySet()) {
            allSupportedTypes.add(m);
        }
        for(Representation c: Representation.values()){
            allSupportedTypes.add(c.asMediaType());
        }

        MediaType[] allSupportedMedia = allSupportedTypes.toArray(new MediaType[allSupportedTypes.size()]);
        allVariants = Variant.mediaTypes(allSupportedMedia).add().build();
        return allVariants;
    }

    protected Response buildResponse(Variant v){
        if (v == null)
            return Response.notAcceptable(allSupportedTypes()).build();

        // is it just redirect...?
        MediaType requestedMedia = v.getMediaType();
        if(model.getUserMedia().keySet().contains(requestedMedia)){
            return Response.seeOther(model.getUserMedia().get(requestedMedia)).build();
        }

        // ...no, we have to act accordingly
        Representation r;
        if ((r = Representation.valueOf(v)) == null)
            return Response.notAcceptable(allSupportedTypes()).build();

        if (r == Representation.TEXT_HTML || !(model.getXml()==null || "".equals(model.getXml()))) {
            Object entity = r.render(model);
            return Response.ok(entity).type(r.asMediaType()).build();
        } else
            return Response.noContent().build();
    }
}
