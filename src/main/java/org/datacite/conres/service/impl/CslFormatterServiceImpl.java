package org.datacite.conres.service.impl;

import org.codehaus.jettison.json.JSONObject;
import org.datacite.conres.service.CslFormatterService;

public class CslFormatterServiceImpl implements CslFormatterService {
    @Override
    public String format(JSONObject cslJson, String cslStyle, String cslLocale) {
        return cslStyle + ", " + cslLocale + "\n" + cslJson;
    }
}
