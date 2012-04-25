package org.datacite.conres.service;

import org.codehaus.jettison.json.JSONObject;

public interface CslFormatterService {
    String format(JSONObject cslJson, String cslStyle, String cslLocale);
}
