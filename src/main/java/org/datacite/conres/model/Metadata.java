package org.datacite.conres.model;

import nu.xom.*;
import org.datacite.conres.view.Representation;

import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.*;

/**
 * Represents metadata record (parsed from XML)
 */
public class Metadata {
    private byte[] xml;
    private String doi;
    private Map<MediaType, URI> userMedia;
    private Document document;
    private List<String> creators;
    private String publicationYear;
    private List<Pair> titles;
    private String publisher;
    private List<Pair> resourceTypes;
    private List<Pair> descriptions;
    private List<Pair> subjects;
    private List<String> sizes;
    private String rights;
    private List<Pair> dates;
    private String language;
    private List<String> formats;
    private String version;
    private List<Pair> alternateIdentifiers;
    private List<Pair> relatedIdentifiers;
    private List<Pair> contributors;
    private String allocatorName;
    private String datacentreName;
    private boolean xmlPresent;
    private String randomId;

    private String contextPath;
    private String cslStyle;
    private String cslLocale;
    private Date uploaded;

    public Metadata(String doi,
                    byte[] xml,
                    Map<MediaType, URI> userMedia,
                    String contextPath,
                    String allocatorName,
                    String datacentreName,
                    String cslStyle,
                    String cslLocale,
                    Date uploaded) {
        this.doi = doi;
        this.randomId = UUID.randomUUID().toString();
        this.xml = xml;
        this.xmlPresent = xml!=null && !"".equals(xml);
        this.userMedia = userMedia;
        this.contextPath = contextPath;
        this.allocatorName = allocatorName;
        this.datacentreName = datacentreName;
        this.cslStyle = cslStyle;
        this.cslLocale = cslLocale;
        this.uploaded = uploaded;

        if (xml != null && xml.length > 0){
            Builder parser = new Builder();
            try {
                InputStream inputStream = new ByteArrayInputStream(xml);
                document = parser.build(inputStream, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            this.creators = extractList("creatorName");
            this.publicationYear = extractText("publicationYear");
            this.titles = extractPairs("title", "titleType");
            this.publisher = extractText("publisher");
            this.resourceTypes = extractPairs("resourceType", "resourceTypeGeneral");
            this.descriptions = extractPairs("description", "descriptionType");
            this.subjects = extractPairs("subject", "subjectScheme");
            this.sizes = extractList("size");
            this.rights = extractText("rights");
            this.dates = extractPairs("date", "dateType");
            this.language = extractText("language");
            this.formats = extractList("format");
            this.version = extractText("version");
            this.alternateIdentifiers = extractPairs("alternateIdentifier", "alternateIdentifierType");
            this.relatedIdentifiers = extractRelatedIds();
            this.contributors = extractContributors();
        }
    }

    private List<Pair> extractRelatedIds() {
        Nodes nodes = document.query("//*[local-name() = 'relatedIdentifier']");
        List<Pair> result = new ArrayList<Pair>();
        for(int i = 0; i < nodes.size(); i++){
            Node node = nodes.get(i);
            Element el = (Element) node;
            Attribute idType = el.getAttribute("relatedIdentifierType");
            Attribute relType = el.getAttribute("relationType");
            String idTypeStr = "";
            String relTypeStr = "";
            if (relType != null)
                relTypeStr = relType.getValue().trim();
            if (idType != null)
                idTypeStr = idType.getValue().trim();
            result.add(new Pair(relTypeStr, idTypeStr.toLowerCase() + ":" + node.getValue().trim()));
        }

        return result;
    }

    private List<Pair> extractContributors() {
        Nodes nodes = document.query("//*[local-name() = 'contributor']");
        List<Pair> result = new ArrayList<Pair>();
        for(int i = 0; i < nodes.size(); i++){
            Node node = nodes.get(i);
            Element el = (Element) node;
            Attribute conType = el.getAttribute("contributorType");
            String conTypeStr = "";
            if (conType != null)
                conTypeStr = conType.getValue().trim();
            result.add(new Pair(conTypeStr, node.getValue().trim()));
        }

        return result;
    }

    private List<Pair> extractPairs(String elementName, String attributeName) {
        List<Pair> result = new ArrayList<Pair>();
        Nodes nodes = document.query("//*[local-name() = '" + elementName + "']");

        for(int i = 0; i < nodes.size(); i++){
            Node node = nodes.get(i);
            Element el = (Element) node;
            Attribute attribute = el.getAttribute(attributeName);
            String attr = "";
            if (attribute != null)
                attr = attribute.getValue();
            result.add(new Pair(attr, node.getValue().trim()));
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

    public byte[] getXml() {
        return xml;
    }

    public List<String> getCreators() {
        return creators;
    }

    public String getPublicationYear() {
        return publicationYear;
    }

    public List<Pair> getTitles() {
        return titles;
    }

    public String getPublisher() {
        return publisher;
    }

    public List<Pair> getResourceTypes() {
        return resourceTypes;
    }

    public List<Pair> getDescriptions() {
        return descriptions;
    }

    public List<Pair> getSubjects() {
        return subjects;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public String getRights() {
        return rights;
    }

    public List<Pair> getDates() {
        return dates;
    }

    public String getLanguage() {
        return language;
    }

    public List<String> getFormats() {
        return formats;
    }

    public String getVersion() {
        return version;
    }

    public List<Pair> getAlternateIdentifiers() {
        return alternateIdentifiers;
    }

    public List<Pair> getRelatedIdentifiers() {
        return relatedIdentifiers;
    }

    public List<Pair> getContributors() {
        return contributors;
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getAllocatorName() {
        return allocatorName;
    }

    public String getDatacentreName() {
        return datacentreName;
    }

    public boolean isXmlPresent() {
        return xmlPresent;
    }

    public String getRandomId() {
        return randomId;
    }

    public String getCslStyle() {
        return cslStyle;
    }

    public String getCslLocale() {
        return cslLocale;
    }

    public Map<MediaType, URI> getUserMedia() {
        return userMedia;
    }

    public String getFirstTitle() {
        if (titles == null || titles.size() == 0)
            return null;
        return titles.get(0).getValue();
    }

    public List<String> getAllMedia(){
        List<String> allSupportedTypes = new ArrayList<String >();
        for (MediaType m : userMedia.keySet()) {
            allSupportedTypes.add(m.toString());
        }

        if (xmlPresent)
            for(Representation c: Representation.values()) {
                allSupportedTypes.add(c.toString());
            }

        return allSupportedTypes;
    }

    public Date getUploaded() {
        return uploaded;
    }
}
