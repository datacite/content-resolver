package org.datacite.conres.view;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.datacite.conres.model.Metadata;

public class CslJsonRepresentation {
    public static JSONObject writeJSON(Metadata mr) {
        try {
            JSONArray authors = new JSONArray();
            for (String  p : mr.getCreators()) {
                authors.put(new JSONObject().put("literal", p));
            }
            return new JSONObject()
                    .put("type", "misc")
                    .put("DOI", mr.getDoi())
                    .put("URL", "http://dx.doi.org/" + mr.getDoi())
                    .put("title", mr.getTitles().get(0).getValue())
                    .put("publisher", mr.getPublisher())
                    .put("issued", new JSONObject().put("raw", mr.getPublicationYear()))
                    .put("author", authors);
        } catch (JSONException je) {
            return null;
        }
    }
}
