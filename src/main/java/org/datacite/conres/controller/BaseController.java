package org.datacite.conres.controller;

import org.apache.log4j.Logger;
import org.datacite.conres.model.Metadata;
import org.datacite.conres.service.SearchService;
import org.datacite.conres.service.SearchServiceFactory;
import org.datacite.conres.view.Representation;

import javax.ws.rs.core.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseController {
    static final Logger log4j = Logger.getLogger(BaseController.class);
    private List<Variant> allVariants;
    protected Metadata model;
    protected final String acceptHeader;

    public BaseController(String doi, UriInfo uriInfo, HttpHeaders httpHeaders) {
        acceptHeader = getAcceptHeader(httpHeaders);
        String doiPrefix = doi.substring(0, doi.indexOf("/"));
        SearchService service = SearchServiceFactory.getInstance(doiPrefix);
        model = service.getMetadata(doi, uriInfo.getBaseUri().getPath(), acceptHeader);
    }

    private String getAcceptHeader(HttpHeaders httpHeaders) {
        List<String> acceptList = httpHeaders.getRequestHeader("Accept");
        String acceptHeader;
        if (acceptList.size() > 0)
            acceptHeader = acceptList.get(0);
        else
            acceptHeader = null;
        return acceptHeader;
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
        // is it just redirect...?
        MediaType requestedMedia = v.getMediaType();
        if(model.getUserMedia().keySet().contains(requestedMedia)){
            URI location = model.getUserMedia().get(requestedMedia);
            log4j.info("Redirecting: " +  model.getDoi() +
                       " as " + requestedMedia +
                       " to " + location);
            return Response.seeOther(location).build();
        }

        // ...no, we have to act accordingly
        Representation r;
        if ((r = Representation.valueOf(v)) == null) {
            log4j.info("Not acceptable: " +  model.getDoi() + " as " + acceptHeader);
            return Response.notAcceptable(allSupportedTypes()).build();
        }

        MediaType type = r.asMediaType();
        if (r == Representation.TEXT_HTML || model.isXmlPresent()) {
            Object entity = r.render(model);
            log4j.info("Rendering: " +  model.getDoi() + " as " + type);
            return Response.ok(entity).type(type).build();
        } else {
            log4j.info("No metadata for: " +  model.getDoi() + " as " + type);
            return Response.noContent().build();
        }
    }
}
