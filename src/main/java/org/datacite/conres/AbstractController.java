package org.datacite.conres;

import javax.ws.rs.core.MediaType;
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

    public AbstractController(String doi) {
        this.doi = doi;
        String doiPrefix = doi.substring(0, doi.indexOf("/"));
        service = SearchServiceFactory.getInstance(doiPrefix);
        doiRegistered = service.isDoiRegistered(doi);
        if (doiRegistered) {
            userMedia = service.getMedia(doi);
            allocatorName = service.getAllocatorName(doi);
            datacentreName = service.getDatacentreName(doi);
        }
    }

    public List<Variant> allSupportedTypes() {
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
}
