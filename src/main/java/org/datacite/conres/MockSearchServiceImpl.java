package org.datacite.conres;

import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class MockSearchServiceImpl implements SearchService {
    public static final String TEST_PREFIX = "10.5072";
    public static final String TEST_DOI = "10.5072/1";
    public static final String TEST_DOI_NON_EXISTENT = "10.5072/2";
    public static final String TEST_DOI_NO_XML = "10.5072/3";

    public static final String TEST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<resource xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
            " xsi:schemaLocation=\"http://datacite.org/schema/kernel-2.2 http://schema.datacite.org/meta/kernel-2.2/metadata.xsd\"" +
            " xmlns=\"http://datacite.org/schema/kernel-2.2\"" +
            " metadataVersionNumber=\"0\" lastMetadataUpdate=\"2011-11-10\">" +
            "<identifier identifierType=\"DOI\">10.5072/1</identifier>" +
            "<creators>" +
            "<creator>" +
            "<creatorName>University of Manchester. Cathie Marsh Centre for" +
            " Census and Survey Research. ESDS Government" +
            "</creatorName>" +
            "</creator>" +
            "</creators>" +
            "<titles>" +
            "<title>British Crime Survey 2007-2008: Unrestricted Access Teaching" +
            " Dataset" +
            "</title>" +
            "</titles>" +
            "<publisher>UK Data Archive, University of Essex</publisher>" +
            "<publicationYear>2011</publicationYear>" +
            "</resource>";

    public static final MediaType APPLICATION_PDF = new MediaType("application","pdf");
    public static URI HTTP_EXAMPLE_COM_A_PDF;

    static {
        try {
            HTTP_EXAMPLE_COM_A_PDF = new URI("http://example.com/a.pdf");
        } catch (URISyntaxException e) {
            HTTP_EXAMPLE_COM_A_PDF = null;
        }
    }

    @Override
    public boolean isDoiRegistered(String doi) {
        return doi.equals(TEST_DOI) || doi.equals(TEST_DOI_NO_XML);
    }

    @Override
    public String getAllocatorName(String doi) {
        return "The British Library";
    }

    @Override
    public String getXml(String doi) {
        if (doi.equals(TEST_DOI))
            return TEST_XML;
        else if (doi.equals(TEST_DOI_NO_XML))
            return "";
        else
            return null;
    }

    @Override
    public Map<MediaType, URI> getMedia(String doi) {
        Map<MediaType, URI> media = new HashMap<MediaType, URI>();
        media.put(APPLICATION_PDF, HTTP_EXAMPLE_COM_A_PDF);
        try {
            media.put(new MediaType("image","gif"), new URI("http://example.com/a.gif"));
        } catch (URISyntaxException e) {
        }
        return media;
    }
}
