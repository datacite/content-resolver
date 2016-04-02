package org.datacite.conres.view;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.datacite.conres.model.Model;
import org.datacite.conres.model.Pair;

public class CslJsonRepresentation {
    public static JSONObject writeJSON(Model mr) {
        try {
            JSONArray authors = new JSONArray();
            for (String  p : mr.getCreators()) {
                authors.put(new JSONObject().put("literal", p));
            }

            String type = "misc";
            for (Pair p : mr.getResourceTypes()){
                if (p.getKey().toUpperCase().equals("DATASET")){
                    type = "dataset";
                    break;
                }
            }

            return new JSONObject()
                    .put("type", type)
                    .put("DOI", mr.getDoi())
                    .put("URL", "https://doi.org/" + mr.getDoi())
                    .put("title", mr.getTitles().get(0).getValue())
                    .put("publisher", mr.getPublisher())
                    .put("issued", new JSONObject().put("raw", mr.getPublicationYear()))
                    .put("author", authors);
        } catch (JSONException je) {
            return null;
        }
    }
}
