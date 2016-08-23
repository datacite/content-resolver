package org.datacite.conres.controller;

import com.google.common.cache.CacheStats;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.log4j.Logger;
import org.datacite.conres.Configuration;
import org.datacite.conres.service.impl.SearchServiceImpl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;

@Path("/status")
public class StatusController {
    private static final Logger log4j = Logger.getLogger(StatusController.class);

    @GET
    public Response get() throws URISyntaxException {
        CacheStats stats = SearchServiceImpl.solrResponsesCache.stats();
        log4j.debug("SOLR cache: number of times Cache lookup methods have returned a cached value = " + stats.hitCount());
        log4j.debug("SOLR cache: ratio of cache requests which were hits = " + stats.hitRate());
        log4j.debug("SOLR cache: number of times an entry has been evicted = " + stats.evictionCount());
        log4j.debug("SOLR cache: average time spent loading new values = " + stats.averageLoadPenalty());

        Client client = Client.create();
        WebResource webResource = client.resource(Configuration.SOLR_STATUS_URL);
        ClientResponse cr = webResource.get(ClientResponse.class);
        if (cr.getStatus() == 200)
            return Response.ok("OK").build();
        else
            return Response.serverError().build();
    }
}
