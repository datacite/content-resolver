package org.datacite.conres.service.impl;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.datacite.conres.service.SearchService;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class SearchServiceImpl implements SearchService {

    //public static final String SOLR_URL = "http://dev.datacite.org/search/api";
    public static final String SOLR_URL = "http://search.datacite.org/api";
    public static final String DATACITE_DEFAULT_ENCODING = "UTF-8";
    public static final String XML_FACET = "xml";
    public static final String ALLOCATOR_FACET = "allocator";
    public static final String DATACENTRE_FACET = "datacentre";
    public static final String DOI_FACET = "doi";
    public static final String MEDIA_FACET = "media";
    //public static final String SAMPLE_DOI = "10.9999/AAAA";
    public static final String SAMPLE_DOI = "10.1594/PANGAEA.251240";

    private String getUrl(String doi, String facet) throws UnsupportedEncodingException {
        return SOLR_URL +  "?q=doi:%22"+ URLEncoder.encode(doi, DATACITE_DEFAULT_ENCODING) +"%22&fl="
                + facet + "&wt=csv&csv.header=false";
    }

    private String getFacet(String doi, String facetName){
        Client c = Client.create();
        String result;
        WebResource r;
        String url;
        try {
            url = getUrl(doi, facetName);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        r = c.resource(url);
        result = r.get(String.class);

        return result;
    }

    private MediaType getMediaType(String mediaTypeStr){
        String[] subtypes = mediaTypeStr.split("/");
        return new MediaType(subtypes[0],subtypes[1]);
    }

    @Override
    public boolean isDoiRegistered(String doi) {
        return getFacet(doi, DOI_FACET).length() !=0;
    }

    @Override
    public String getAllocatorName(String doi) {
        String n = getFacet(doi, ALLOCATOR_FACET);
        return n.substring(n.indexOf("-") + 1).trim();
    }

    @Override
    public String getDatacentreName(String doi) {
        String n = getFacet(doi, DATACENTRE_FACET);
        return n.substring(n.indexOf("-") + 1).trim();
    }

    @Override
    public String getXml(String doi) {
        try {
            return new String(DatatypeConverter.parseBase64Binary(getFacet(doi, XML_FACET)), DATACITE_DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<MediaType, URI> getMedia(String doi) {
        HashMap<MediaType, URI> result = new HashMap<MediaType, URI>();
        String allMedia = getFacet(doi, MEDIA_FACET).trim();

        if (!allMedia.startsWith("\"")) {
            registerMedia(result, allMedia);
        }
        else if (allMedia.length() > 3) {
            allMedia = allMedia.substring(1);
            allMedia = allMedia.substring(0, allMedia.length() - 1);
            String[] mediaList = allMedia.split(",");
            for (String media : mediaList) {
                registerMedia(result, media);
            }
        }

        return result;
    }

    private void registerMedia(HashMap<MediaType, URI> result, String media) {
        int firstComma = media.indexOf(":");
        URI uri = null;
        try {
            String uriStr = media.substring(firstComma+1);
            uri = new URI(uriStr);
        } catch (URISyntaxException e) {
            // TODO log rubbish URI
        }
        if (uri != null) {
            String mediaStr = media.substring(0, firstComma);
            result.put(getMediaType(mediaStr), uri);
        }
    }

}
