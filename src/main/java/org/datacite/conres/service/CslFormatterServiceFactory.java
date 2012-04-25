package org.datacite.conres.service;

import org.datacite.conres.service.impl.CslFormatterServiceImpl;

public class CslFormatterServiceFactory {
    private static final CslFormatterService instance = new CslFormatterServiceImpl();

    public static CslFormatterService getInstance() {
        return instance;
    }
}
