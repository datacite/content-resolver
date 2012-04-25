package org.datacite.conres.controller;

import org.datacite.conres.model.Metadata;
import org.datacite.conres.service.SearchService;
import org.datacite.conres.service.SearchServiceFactory;
import org.datacite.conres.view.Representation;

import javax.ws.rs.core.*;
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
    private List<Variant> allVariants;
    private String acceptHeader;

    public AbstractController(String doi, UriInfo uriInfo, HttpHeaders httpHeaders) {
        this.doi = doi;
        this.uriInfo = uriInfo;
        List<String> acceptList = httpHeaders.getRequestHeader("Accept");
        if (acceptList.size() > 0)
            acceptHeader = acceptList.get(0);
        else
            acceptHeader = null;
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
                datacentreName,
                extractBiblioAttr(acceptHeader, "style"),
                extractBiblioAttr(acceptHeader, "locale"));
    }

    private String extractBiblioAttr(String header, String attr) {
        String result = "";
        for(String h : header.split(",")){
            if (h.trim().startsWith(Representation.TEXT_BIBLIOGRAPHY.toString())){
                for(String s : h.split(";")) {
                    if (s.trim().startsWith(attr)){
                        String[] l = s.split("=");
                        result = l.length == 2 ? l[1].trim() : "";
                        break;
                    }
                }
                break;
            }
        }
        return result;
    }

    protected List<Variant> allSupportedTypes() {
        if (allVariants != null)
            return allVariants;

        List<MediaType> allSupportedTypes = new ArrayList<MediaType>();
        for (MediaType m : userMedia.keySet()) {
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
