package org.datacite.conres;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.test.framework.JerseyTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LinkControllerTest extends JerseyTest {
    public LinkControllerTest()throws Exception {
        super("org.datacite.conres");
    }

    @Test
    public void testHelloWorld() {
        WebResource webResource = resource().path(Representation.APPLICATION_DATACITE_XML + "/" +
                MockSearchServiceImpl.TEST_DOI);
        webResource.addFilter(new LoggingFilter());

        ClientResponse response = webResource.accept(Representation.APPLICATION_DATACITE_XML.asMediaType()).get(ClientResponse.class);
        assertEquals(Representation.APPLICATION_DATACITE_XML.asMediaType(), response.getType());
    }
}
