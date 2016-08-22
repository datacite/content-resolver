package org.datacite.conres.view;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.datacite.conres.model.Model;
import org.datacite.conres.model.Pair;

import java.util.List;
import java.util.Arrays;

public class CslJsonRepresentation {
    public static JSONObject writeJSON(Model mr) {
        try {
            JSONArray authors = new JSONArray();
            for (String  p : mr.getCreators()) {
                if (p.contains(",")) {
                    List<String> pList = Arrays.asList(p.split(", ", 2));
                    authors.put(new JSONObject()
                        .put("family", pList.get(0))
                        .put("given", pList.get(1)));
                } else {
                    authors.put(new JSONObject().put("literal", p));
                }
            }

            JSONObject issued = new JSONObject();
            Integer year = Integer.valueOf(mr.getPublicationYear());
            issued.put("date-parts", new JSONArray().put(new JSONArray().put(year)));

            String type = "misc";
            for (Pair p : mr.getResourceTypes()) {
                if (p.getKey().toUpperCase().equals("DATASET")) {
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
                .put("issued", issued)
                .put("author", authors);
        } catch (JSONException je) {
            return null;
        }
    }
}
