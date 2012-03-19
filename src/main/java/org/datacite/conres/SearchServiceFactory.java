package org.datacite.conres;

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
