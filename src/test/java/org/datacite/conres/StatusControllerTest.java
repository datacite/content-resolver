package org.datacite.conres;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.freemarker.FreemarkerViewProcessor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StatusControllerTest extends JerseyTest {
    public StatusControllerTest()throws Exception {
        super(new WebAppDescriptor.Builder("org.datacite.conres")
                .initParam(FreemarkerViewProcessor.FREEMARKER_TEMPLATES_BASE_PATH, "freemarker")
                .build());
    }

    @Test
    public void test200() {
        WebResource webResource = resource().path("/heartbeat");
        webResource.addFilter(new LoggingFilter());
        ClientResponse response = webResource.get(ClientResponse.class);
        assertEquals(200, response.getStatus());
    }
}
