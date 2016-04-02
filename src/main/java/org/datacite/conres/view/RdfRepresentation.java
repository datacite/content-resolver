package org.datacite.conres.view;

import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.OWL;
import org.datacite.conres.model.Model;

import java.io.StringWriter;

public class RdfRepresentation {
    public static String writeXML(Model m){
        com.hp.hpl.jena.rdf.model.Model model = getRdfModel(m);
        StringWriter writer = new StringWriter();
        model.write(writer, "RDF/XML");
        return writer.toString();
    }

    public static String writeTurtle(Model m){
        com.hp.hpl.jena.rdf.model.Model model = getRdfModel(m);
        StringWriter writer = new StringWriter();
        model.write(writer, "TURTLE");
        return writer.toString();
    }

    private static com.hp.hpl.jena.rdf.model.Model getRdfModel(Model m) {
        com.hp.hpl.jena.rdf.model.Model model = ModelFactory.createDefaultModel();
        String doiUri = "https://doi.org/" + m.getDoi();
        String doi = "doi:" + m.getDoi();
        String infoDoi = "info:doi/" + m.getDoi();
        Resource dataset = model.createResource(doiUri)
                .addProperty(DCTerms.identifier, m.getDoi())
                .addProperty(OWL.sameAs, infoDoi)
                .addProperty(OWL.sameAs, doi)
                .addProperty(DCTerms.date, m.getPublicationYear())
                .addProperty(DCTerms.title, m.getTitles().get(0).getValue())
                .addProperty(DCTerms.publisher, m.getPublisher());
        for(String s : m.getCreators()){
            dataset.addProperty(DCTerms.creator, s);
        }

        return model;
    }
}
