package org.datacite.conres;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.freemarker.FreemarkerViewProcessor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.datacite.conres.service.impl.MockSearchServiceImpl;
import org.datacite.conres.view.Representation;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ContentControllerTest extends JerseyTest{
    public ContentControllerTest()throws Exception {
        super(new WebAppDescriptor.Builder("org.datacite.conres")
                .initParam(FreemarkerViewProcessor.FREEMARKER_TEMPLATES_BASE_PATH, "freemarker")
                .build());
    }

    @Test
    public void test204() {
        WebResource webResource = resource().path(MockSearchServiceImpl.TEST_DOI_NO_XML);
        webResource.addFilter(new LoggingFilter());
        ClientResponse response = webResource.accept(Representation.APPLICATION_DATACITE_XML.asMediaType()).get(ClientResponse.class);
        assertEquals(204, response.getStatus());
    }

    @Test
    public void test404() {
        WebResource webResource = resource().path(MockSearchServiceImpl.TEST_DOI_NON_EXISTENT);
        webResource.addFilter(new LoggingFilter());
        ClientResponse response = webResource.accept(Representation.APPLICATION_DATACITE_XML.asMediaType()).get(ClientResponse.class);
        assertEquals(404, response.getStatus());
    }

    @Test
    public void test406() {
        WebResource webResource = resource().path(MockSearchServiceImpl.TEST_DOI);
        webResource.addFilter(new LoggingFilter());
        ClientResponse response = webResource.accept("invalid/invalid").get(ClientResponse.class);
        assertEquals(406, response.getStatus());
    }

    private MediaType[] buildAccept(String[] subtypes, String[] weights) {
        if (subtypes == null)
            throw new IllegalArgumentException("subtypes cannot be null");

        if (weights != null && subtypes.length!=weights.length)
            throw new IllegalArgumentException("subtypes and weights must be same size");

        MediaType[] result = new MediaType[subtypes.length];

        for (int i = 0; i < result.length; i++) {
            if (weights != null){
                Map<String, String> q = new HashMap<String, String>();
                q.put("q", weights[i]);
                result[i] = new MediaType("application", subtypes[i], q);
            } else {
                result[i] = new MediaType("application", subtypes[i]);
            }
        }
        return result;
    }

    @Test
    public void testAcceptQ1() {
        WebResource webResource = resource().path(MockSearchServiceImpl.TEST_DOI);
        webResource.addFilter(new LoggingFilter());
        MediaType[] formats = buildAccept(new String[]{"rdf+xml", "unixref+xml", "x-datacite+xml"},
                new String[]{"0.9", "0.8", "0.2"});
        ClientResponse response = webResource.accept(formats).get(ClientResponse.class);
        assertEquals(200, response.getStatus());
        assertEquals("application/rdf+xml", response.getType().toString());
    }

    @Test
    public void testAcceptQ2() {
        WebResource webResource = resource().path(MockSearchServiceImpl.TEST_DOI);
        webResource.addFilter(new LoggingFilter());
        MediaType[] formats = buildAccept(new String[]{"fancy+pants", "rdf+xml", "unixref+xml", "x-datacite+xml"},
                new String[]{"1.0", "0.7", "0.2", "0.8"});
        ClientResponse response = webResource.accept(formats).get(ClientResponse.class);
        assertEquals(200, response.getStatus());
        assertEquals("application/x-datacite+xml", response.getType().toString());
    }

    @Test
    public void testAcceptNoQ1() {
        WebResource webResource = resource().path(MockSearchServiceImpl.TEST_DOI);
        webResource.addFilter(new LoggingFilter());
        MediaType[] formats = buildAccept(new String[]{"rdf+xml", "unixref+xml", "x-datacite+xml"}, null);
        ClientResponse response = webResource.accept(formats).get(ClientResponse.class);
        assertEquals(200, response.getStatus());
        assertEquals("application/rdf+xml", response.getType().toString());
    }

    @Test
    public void testAcceptNoQ2() {
        WebResource webResource = resource().path(MockSearchServiceImpl.TEST_DOI);
        webResource.addFilter(new LoggingFilter());
        MediaType[] formats = buildAccept(new String[]{"fancy+pants", "rdf+xml", "unixref+xml", "x-datacite+xml"},null);
        ClientResponse response = webResource.accept(formats).get(ClientResponse.class);
        assertEquals(200, response.getStatus());
        assertEquals("application/rdf+xml", response.getType().toString());
    }

    @Test
    public void testDataCiteXML() {
        WebResource webResource = resource().path(MockSearchServiceImpl.TEST_DOI);
        webResource.addFilter(new LoggingFilter());
        ClientResponse response = webResource.accept(Representation.APPLICATION_DATACITE_XML.asMediaType()).get(ClientResponse.class);
        assertEquals(Representation.APPLICATION_DATACITE_XML.asMediaType(), response.getType());
        assertEquals(MockSearchServiceImpl.TEST_XML, response.getEntity(String.class));
    }

    @Test
    public void testHtml() {
        WebResource webResource = resource().path(MockSearchServiceImpl.TEST_DOI);
        webResource.addFilter(new LoggingFilter());
        String response = webResource.accept(MediaType.TEXT_HTML_TYPE).get(String.class);
        assertTrue(response.contains("10.5072/1"));
    }

    @Test
    public void testCustomMedia() throws Exception{
        WebResource webResource = resource().path(MockSearchServiceImpl.TEST_DOI);
        webResource.addFilter(new LoggingFilter());
        webResource.setProperty(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, false);
        ClientResponse response = webResource.accept(MockSearchServiceImpl.APPLICATION_PDF).get(ClientResponse.class);
        assertEquals(303, response.getStatus());
        assertEquals(MockSearchServiceImpl.HTTP_EXAMPLE_COM_A_PDF, response.getLocation());
    }
}