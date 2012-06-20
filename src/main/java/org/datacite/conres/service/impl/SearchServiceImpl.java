package org.datacite.conres.service.impl;

import com.google.common.base.Charsets;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import nu.xom.*;
import org.datacite.conres.Configuration;
import org.datacite.conres.model.Model;
import org.datacite.conres.service.SearchService;
import org.datacite.conres.view.Representation;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class SearchServiceImpl implements SearchService {
    private static Client client = Client.create();
    static DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTimeParser();
    static LoadingCache<String, String> solrResponsesCache = CacheBuilder.newBuilder()
            .maximumSize(Configuration.SOLR_CACHE_SIZE)
            .expireAfterWrite(60, TimeUnit.SECONDS)
            .build(new CacheLoader<String, String>() {
                        public String load(String key) {
                            return getRawMetadata(key);
                        }
            });
    private Document document;

    private static String getUrl(String doi) throws UnsupportedEncodingException {
        return Configuration.SOLR_API_URL +  "?q=doi:%22"+ URLEncoder.encode(doi, Charsets.UTF_8.name()) +
                "%22&fl=allocator,datacentre,media,xml,uploaded&wt=xml";
    }

    private static String getRawMetadata(String doi) {
        String result;
        WebResource r;
        String url;
        try {
            url = getUrl(doi);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        try {
            r = client.resource(url);
        } catch (Exception e) {
            throw new RuntimeException("could not reach search service");
        }
        result = r.get(String.class);

        return result;
    }

    private MediaType getMediaType(String mediaTypeStr){
        String[] subtypes = mediaTypeStr.split("/");
        return new MediaType(subtypes[0],subtypes[1]);
    }

    private void registerMedia(Map<MediaType, URI> result, String media) {
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

    @Override
    public Model getMetadata(String doi, String contextPath, String acceptHeader) {
        String rawMetadata = null;
        try {
            rawMetadata = solrResponsesCache.get(doi);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        if (rawMetadata != null && !"".equals(rawMetadata)){
            Builder parser = new Builder();
            try {
                document = parser.build(rawMetadata, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else
            return null;

        if (extractNumFound() == 0)
            return null;

        String allocatorName = "";
        String datacentreName = "";
        byte[] xml = null;
        Map<MediaType, URI> userMedia = new HashMap<MediaType, URI>();
        Nodes nodes = document.query("//*[local-name() = 'str']");
        for(int i = 0; i < nodes.size(); i++){
            Node node = nodes.get(i);
            Element el = (Element) node;
            Attribute attr = el.getAttribute("name");
            if(attr == null){ // media type
                registerMedia(userMedia, el.getValue());
            } else if (attr.getValue().equals("allocator")){
                allocatorName = el.getValue().substring(el.getValue().indexOf("-") + 1).trim();
            } else if (attr.getValue().equals("datacentre")){
                datacentreName = el.getValue().substring(el.getValue().indexOf("-") + 1).trim();
            } else if (attr.getValue().equals("xml")){
                xml = DatatypeConverter.parseBase64Binary(el.getValue());
            }
        }

        Nodes dates = document.query("//*[local-name() = 'date']");
        Date uploaded = null;
        for(int i = 0; i < dates.size(); i++){
            Node node = dates.get(i);
            Element el = (Element) node;
            Attribute attr = el.getAttribute("name");
            if (attr.getValue().equals("uploaded")){
                uploaded = dateTimeFormatter.parseDateTime(el.getValue()).toDate();
            }
        }

        return new Model(doi,
                xml,
                userMedia,
                contextPath,
                allocatorName,
                datacentreName,
                extractBiblioAttr(acceptHeader, "style"),
                extractBiblioAttr(acceptHeader, "locale"),
                uploaded);
    }

    private int extractNumFound() {
        Nodes nodes = document.query("//*[local-name() = 'result']");
        if (nodes.size() == 1){
            Element el = (Element) nodes.get(0);
            Attribute attr = el.getAttribute("numFound");
            return Integer.valueOf(attr.getValue());
        } else return 0;
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
}
