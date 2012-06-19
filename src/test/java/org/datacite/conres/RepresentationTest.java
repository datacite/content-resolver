package org.datacite.conres;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.datacite.conres.model.Metadata;
import org.datacite.conres.service.impl.MockSearchServiceImpl;
import org.datacite.conres.view.Representation;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RepresentationTest {

    @Test
    public void CslTest() throws JSONException {
        Representation r = Representation.CITEPROC_JSON;
        byte[] xml = TestUtil.loadData(getClass(), "/test1.xml");
        assertNotNull(xml);
        assertTrue(xml.length != 0);
        Map<MediaType,URI> media = new HashMap<MediaType, URI>();
        Metadata m = new Metadata(MockSearchServiceImpl.TEST_DOI, xml, media, null, null, null, null, null, null);

        JSONObject csl = (JSONObject) r.render(m);
        assertEquals(csl.get("DOI"), MockSearchServiceImpl.TEST_DOI);
    }
}
