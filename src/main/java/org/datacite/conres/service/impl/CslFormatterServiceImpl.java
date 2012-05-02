package org.datacite.conres.service.impl;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.codehaus.jettison.json.JSONObject;
import org.datacite.conres.service.CslFormatterService;

import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class CslFormatterServiceImpl implements CslFormatterService {
    private static Client client = Client.create();

    @Override
    public String format(JSONObject cslJson, String cslStyle, String cslLocale) {
        String url = (String) SearchServiceImpl.prop.get("citeproc.server.url");
        // TODO handle default values for CSL style and locale
        WebResource r = null;
        try {
            r = client.resource(url +
                    "?style=" + URLEncoder.encode(cslStyle, SearchServiceImpl.DATACITE_DEFAULT_ENCODING)+
                    "&lang=" + URLEncoder.encode(cslLocale, SearchServiceImpl.DATACITE_DEFAULT_ENCODING));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return r.type(MediaType.APPLICATION_JSON_TYPE).post(String.class, cslJson);
    }
}
