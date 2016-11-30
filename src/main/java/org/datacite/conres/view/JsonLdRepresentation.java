package org.datacite.conres.view;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.datacite.conres.model.Model;
import org.datacite.conres.model.Pair;

import java.util.List;
import java.util.Arrays;

public class JsonLdRepresentation {
    public static JSONObject writeJSON(Model mr) {
        try {
            JSONArray authors = new JSONArray();
            for (String  p : mr.getCreators()) {
                if (p.contains(",")) {
                    List<String> pList = Arrays.asList(p.split(", ", 2));
                    authors.put(new JSONObject()
                        .put("@type", "Person")
                        .put("familyName", pList.get(0))
                        .put("givenName", pList.get(1)));
                } else {
                    authors.put(new JSONObject()
                        .put("@type", "Organisation")
                        .put("name", p));
                }
            }

            JSONObject issued = new JSONObject();
            Integer year = Integer.valueOf(mr.getPublicationYear());
            issued.put("date-parts", new JSONArray().put(new JSONArray().put(year)));

            String type = "CreativeWork";
            for (Pair p : mr.getResourceTypes()) {
                if (p.getKey().toUpperCase().equals("DATASET")) {
                    type = "Dataset";
                    break;
                }
                else if (p.getKey().toUpperCase().equals("TEXT")) {
                    type = "Article";
                    break;
                }
                else if (p.getKey().toUpperCase().equals("SOFTWARE")) {
                    type = "SoftwareApplication";
                    break;
                }
            }

            return new JSONObject()
                .put("@context", "http://schema.org")
                .put("@type", type)
                .put("@id", "https://doi.org/" + mr.getDoi())
                .put("name", mr.getTitles().get(0).getValue())
                .put("author", authors)
                .put("publisher", mr.getPublisher())
                .put("datePublished", mr.getPublicationYear())
                .put("version", mr.getVersion())
                .put("description", mr.getDescriptions().get(0).getValue());
        } catch (JSONException je) {
            return null;
        }
    }
}
