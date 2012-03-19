package org.datacite.conres;

import nu.xom.*;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents metadata record (parsed from XML)
 */
public class Metadata {
    private String xml;
    private String doi;
    private Map<MediaType, URI> media;
    private Document document;
    private List<String> creators;
    private String publicationYear;
    private Map<String, String> titles;
    private String publisher;

    public Metadata(String doi, String xml, Map<MediaType, URI> media) {
        this.doi = doi;
        this.xml = xml;
        this.media = media;

        Builder parser = new Builder();
        try {
            document = parser.build(xml, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.creators = extractList("creatorName");
        this.publicationYear = extractText("publicationYear");
        this.titles = extractPairs("title", "titleType");
        this.publisher = extractText("publisher");
    }

    private Map<String, String> extractPairs(String elementName, String attributeName) {
        Map<String, String> result = new HashMap<String, String>();
        Nodes nodes = document.query("//*[local-name() = '" + elementName + "']");

        for(int i = 0; i < nodes.size(); i++){
            Node node = nodes.get(i);
            String attr = node.getDocument().getRootElement().getAttributeValue(attributeName);
            if (attr == null)
                attr = "";
            result.put(attr, node.getValue().trim());
        }

        return result;
    }

    private String extractText(String elementName) {
        StringBuilder sb = new StringBuilder();
        Nodes nodes = document.query("//*[local-name() = '" + elementName + "']");

        for(int i = 0; i < nodes.size(); i++){
            Node node = nodes.get(i);
            sb.append(node.getValue().trim());
        }

        return sb.toString();
    }

    private List<String> extractList(String elementName){
        List<String> result = new ArrayList<String>();
        Nodes nodes = document.query("//*[local-name() = '" + elementName + "']");

        for(int i = 0; i < nodes.size(); i++){
            Node node = nodes.get(i);
            result.add(node.getValue().trim());
        }

        return result;
    }

    public String getDoi() {
        return doi;
    }

    public String getXml() {
        return xml;
    }

    public List<String> getCreators() {
        return creators;
    }

    public String getPublicationYear() {
        return publicationYear;
    }

    public Map<String, String> getTitles() {
        return titles;
    }

    public String getPublisher() {
        return publisher;
    }

    public List<String> getAllMedia(){
        List<String> allSupportedTypes = new ArrayList<String >();
        for (MediaType m : media.keySet()) {
            allSupportedTypes.add(m.toString());
        }
        for(Representation c: Representation.values()) {
            allSupportedTypes.add(c.toString());
        }

        return allSupportedTypes;
    }
}
