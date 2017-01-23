package org.datacite.conres.controller;

import org.apache.log4j.Logger;
import com.bugsnag.Bugsnag;
import org.datacite.conres.Configuration;
import org.datacite.conres.model.Model;
import org.datacite.conres.service.SearchService;
import org.datacite.conres.service.SearchServiceFactory;
import org.datacite.conres.view.Representation;

import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseController {
    static final Logger log4j = Logger.getLogger(BaseController.class);
    // Bugsnag bugsnag = new Bugsnag(Configuration.BUGSNAG_KEY);

    private List<Variant> allVariants;
    protected Model model;
    protected final String acceptHeader;

    public BaseController(String doi, HttpHeaders httpHeaders) {
        acceptHeader = getAcceptHeader(httpHeaders);
        String doiPrefix = doi.substring(0, 7);
        SearchService service = SearchServiceFactory.getInstance(doiPrefix);
        model = service.getMetadata(doi, Configuration.APP_CONTEXT, acceptHeader);
    }

    private String getAcceptHeader(HttpHeaders httpHeaders) {
        List<String> acceptList = httpHeaders.getRequestHeader("Accept");
        String acceptHeader;
        if (acceptList != null && acceptList.size() > 0)
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
            log4j.error("Not acceptable: " +  model.getDoi() + " as " + acceptHeader);
            return Response.notAcceptable(allSupportedTypes()).build();
        }

        MediaType type = r.asMediaType();
        if (r == Representation.TEXT_HTML || model.isXmlPresent()) {
            Object entity = r.render(model);
            log4j.info("Rendering: " +  model.getDoi() + " as " + type);
            CacheControl cc = new CacheControl();
            cc.setMaxAge(Configuration.CACHE_CONTROL_MAX_AGE);

            // character encoding
            String charset = "";
            switch(type.toString()) {
                case "text/x-bibliography" :
                    charset = "; charset=iso-8859-1";
                    break;
                case "application/vnd.citationstyles.csl+json" :
                case "application/citeproc+json" :
                case "application/vnd.schemaorg.ld+json" :
                case "text/turtle" :
                    charset = "; charset=UTF-8";
                    break;
                case "application/x-research-info-systems" :
                    charset = "; charset=charset=windows-1252";
            }

            return Response.ok(entity).
                    type(type).
                    cacheControl(cc).
                    lastModified(model.getUploaded()).
                    header(HttpHeaders.CONTENT_TYPE, type + charset).
                    build();
        } else {
            log4j.info("No metadata for: " +  model.getDoi() + " as " + type);
            return Response.noContent().build();
        }
    }
}
