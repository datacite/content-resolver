package org.datacite.conres.service.impl;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import nu.xom.*;
import org.datacite.conres.model.Metadata;
import org.datacite.conres.service.SearchService;
import org.datacite.conres.view.Representation;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SearchServiceImpl implements SearchService {
    public static final String SOLR_BASE_URL;
    public static final String SOLR_API_URL;
    public static final String SOLR_STATUS_URL;
    public static final String APP_CONTEXT;
    public static final Properties prop; // TODO move to Application class maybe

    static {
        prop = new Properties();
        InputStream in = SearchServiceImpl.class.getResourceAsStream("/config.properties");
        if (in == null)
            throw new RuntimeException("Please setup valid config.properties");
        try {
            prop.load(in);
            SOLR_BASE_URL = (String) prop.get("solr.base.url");
            SOLR_API_URL = SOLR_BASE_URL + "/api";
            SOLR_STATUS_URL = SOLR_BASE_URL + "/status";
            APP_CONTEXT = (String) prop.get("app.context");
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String DATACITE_DEFAULT_ENCODING = "UTF-8";
    public static final String SAMPLE_DOI = "10.1594/PANGAEA.251240";
    private Document document;

    private static Client client = Client.create();

    private String getUrl(String doi) throws UnsupportedEncodingException {
        return SOLR_API_URL +  "?q=doi:%22"+ URLEncoder.encode(doi, DATACITE_DEFAULT_ENCODING) +
                "%22&fl=allocator,datacentre,media,xml&wt=xml";
    }

/*    private String getRawMetadata2(String doi){
        InputStream in;
        int http_status;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(getUrl(doi));
            conn = (HttpURLConnection) url.openConnection();
            in = conn.getInputStream();
            http_status = conn.getResponseCode();
            if (http_status != 200) {
                return null;
            }
            StringWriter sw = new StringWriter();
            sw.a
        } catch (IOException e) {
            return null;
        } finally {
            if (conn != null)
                conn.disconnect();
        }


    }*/

    private String getRawMetadata(String doi) {
        String result;
        WebResource r;
        String url;
        try {
            url = getUrl(doi);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        r = client.resource(url);
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
    public Metadata getMetadata(String doi, String contextPath, String acceptHeader) {
        String rawMetadata = getRawMetadata(doi);

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
        String xml = "";
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
                try {
                    byte[] bytes = DatatypeConverter.parseBase64Binary(el.getValue());
                    int len = bytes.length;
                    if (len > 3 &&
                            bytes[0] == (byte) 0xef &&
                            bytes[1] == (byte) 0xbb &&
                            bytes[2] == (byte) 0xbf) { // UTF-8 BOM, remove
                        len -= 3;
                        System.arraycopy(bytes, 3, bytes, 0, len);
                    }
                    xml = new String(bytes, 0, len,  DATACITE_DEFAULT_ENCODING);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return new Metadata(doi,
                xml,
                userMedia,
                contextPath,//.substring(0, contextPath.length() - 1),
                allocatorName,
                datacentreName,
                extractBiblioAttr(acceptHeader, "style"),
                extractBiblioAttr(acceptHeader, "locale"));
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
