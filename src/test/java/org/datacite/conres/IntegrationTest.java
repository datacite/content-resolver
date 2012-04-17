package org.datacite.conres;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.freemarker.FreemarkerViewProcessor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.datacite.conres.service.impl.SearchServiceImpl;
import org.datacite.conres.view.Representation;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IntegrationTest  extends JerseyTest {

    public IntegrationTest()throws Exception {
        super(new WebAppDescriptor.Builder("org.datacite.conres")
                .initParam(FreemarkerViewProcessor.FREEMARKER_TEMPLATES_BASE_PATH, "freemarker")
                .build());
    }

    @Test
    public void testContentDataCiteXML() {
        WebResource webResource = resource().path(SearchServiceImpl.SAMPLE_DOI);
        webResource.addFilter(new LoggingFilter());
        ClientResponse response = webResource.accept(Representation.APPLICATION_DATACITE_XML.asMediaType()).get(ClientResponse.class);
        assertEquals(Representation.APPLICATION_DATACITE_XML.asMediaType(), response.getType());

        assertTrue(response.getEntity(String.class).contains("schema.datacite.org"));
    }

    @Test
    public void testLinkDataCiteXML() {
        WebResource webResource = resource().path(Representation.APPLICATION_DATACITE_XML
                + "/" + SearchServiceImpl.SAMPLE_DOI);
        webResource.addFilter(new LoggingFilter());
        ClientResponse response = webResource.get(ClientResponse.class);
        assertEquals(Representation.APPLICATION_DATACITE_XML.asMediaType(), response.getType());

        assertTrue(response.getEntity(String.class).contains("schema.datacite.org"));
    }

}
