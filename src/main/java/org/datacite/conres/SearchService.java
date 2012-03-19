package org.datacite.conres;

import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.Map;

/**
 * Responsible for retrieving information from Search service
 */
public interface SearchService {
    /**
     * Check is DOI is registered in Search service
     * @param doi identifier of a dataset
     * @return true is DOI know to Search service
     */
    boolean isDoiRegistered(String doi);

    /**
     * Returns DataCite's member name for given DOI
     * @param doi identifier of dataset
     * @return full name from the organisation
     */
    String getAllocatorName(String doi);

    /**
     * Retrieves native DataCite metadata XML
     * @param doi identifier of dataset
     * @return String containing XML. Empty String if no XML in Search index or any problems.
     */
    String getXml(String doi);

    /**
     * Retrieves user provided userMedia type from the serach service
     * @param doi identifier of dataset
     * @return mapping between types and locations. Empty map if no data or any problems.
     */
    Map<MediaType, URI> getMedia(String doi);
}
