package org.datacite.conres.service;

import org.datacite.conres.service.impl.MockSearchServiceImpl;
import org.datacite.conres.service.impl.SearchServiceImpl;

public class SearchServiceFactory {
    private static SearchService instance = new SearchServiceImpl();
    private static SearchService mockInstance = new MockSearchServiceImpl();

    public static SearchService getInstance(String doi) {
        if (doi.startsWith(MockSearchServiceImpl.TEST_PREFIX))
            return mockInstance;
        else
            return instance;
    }
}
