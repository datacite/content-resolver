package org.datacite.conres.view;

import com.sun.jersey.api.view.Viewable;
import org.datacite.conres.model.Model;
import org.datacite.conres.service.CslFormatterService;
import org.datacite.conres.service.CslFormatterServiceFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Variant;

public enum Representation {
    TEXT_HTML("text","html", new Transformer() {
        @Override public Object transform(Model mr) {
            return new Viewable("/doi_html", mr);
        }
    }),
    APPLICATION_DATACITE_XML ("application","x-datacite+xml", new Transformer() {
        @Override public Object transform(Model mr) {
            return mr.getXml();
        }
    }),
    APPLICATION_VND_DATACITE_XML ("application","vnd.datacite.datacite+xml", new Transformer() {
        @Override public Object transform(Model mr) {
            return mr.getXml();
        }
    }),
    DATACITE_TEXT ("application","x-datacite+text", new Transformer() {
        @Override
        public Object transform(Model mr) {
            return new Viewable("/doi_text", mr);
        }
    }),
    DATACITE_VND_TEXT ("application","vnd.datacite.datacite+text", new Transformer() {
        @Override
        public Object transform(Model mr) {
            return new Viewable("/doi_text", mr);
        }
    }),
    APPLICATION_RDF_XML ("application","rdf+xml", new Transformer() {
        @Override public Object transform(Model mr) {
            return RdfRepresentation.writeXML(mr);
        }
    }),
    RDF_TURTLE ("text","turtle", new Transformer() {
        @Override
        public Object transform(Model mr) {
            return RdfRepresentation.writeTurtle(mr);
        }
    }),
    BIBTEX ("application","x-bibtex", new Transformer() {
        @Override
        public Object transform(Model mr) {
            return new Viewable("/doi_bibtex", mr);
        }
    }),
    RIS ("application","x-research-info-systems", new Transformer() {
        @Override
        public Object transform(Model mr) {
            return new Viewable("/doi_ris", mr);
        }
    }),
    CITEPROC_JSON("application", "citeproc+json", new Transformer() {
        @Override
        public Object transform(Model mr) {
            return CslJsonRepresentation.writeJSON(mr);
        }
    }),
    CSL_JSON("application", "vnd.citationstyles.csl+json", new Transformer() {
        @Override
        public Object transform(Model mr) {
            return CslJsonRepresentation.writeJSON(mr);
        }
    }),
    TEXT_BIBLIOGRAPHY("text", "x-bibliography", new Transformer() {
        @Override
        public Object transform(Model mr) {
            return cslFormatterService.format(CslJsonRepresentation.writeJSON(mr),
                    mr.getCslStyle(),
                    mr.getCslLocale());
        }
    });

    private final String type;
    private final String subtype;
    private final MediaType mediaType;
    private final Transformer transformer;
    private static CslFormatterService cslFormatterService = CslFormatterServiceFactory.getInstance();

    Representation(String type, String subtype, Transformer mt) {
        this.type = type;
        this.subtype = subtype;
        this.mediaType = new MediaType(type, subtype);
        this.transformer = mt;
    }

    public Object render(Model mr){
        return transformer.transform(mr);
    }

    public MediaType asMediaType() {
        return mediaType;
    }

    public static Representation valueOf(Variant v){
        for (Representation representation : values()) {
            if(representation.asMediaType().equals(v.getMediaType()))
                return representation;
        }
        return null;
    }

    @Override
    public String toString() {
        return type + "/" + subtype;
    }
}
