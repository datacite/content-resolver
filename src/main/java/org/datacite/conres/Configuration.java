package org.datacite.conres;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Application-wide configuration
 */
public class Configuration {
    public static final String SOLR_BASE_URL;
    public static final String SOLR_API_URL;
    public static final String SOLR_STATUS_URL;
    public static final String APP_CONTEXT;
    public static final Properties prop;
    public static final int SOLR_CACHE_SIZE;
    public static final int SOLR_CACHE_EXPIRE;
    public static final int CACHE_CONTROL_MAX_AGE;

    static {
        prop = new Properties();
        InputStream in = Configuration.class.getResourceAsStream("/config.properties");
        if (in == null)
            throw new RuntimeException("Please setup valid config.properties");
        try {
            prop.load(in);
            SOLR_BASE_URL = (String) Configuration.prop.get("solr.base.url");
            SOLR_API_URL = Configuration.SOLR_BASE_URL + "/api";
            SOLR_STATUS_URL = Configuration.SOLR_BASE_URL + "/status";
            APP_CONTEXT = (String) Configuration.prop.get("app.context");
            SOLR_CACHE_SIZE = Integer.parseInt((String) Configuration.prop.get("solr.cache.size.items"));
            SOLR_CACHE_EXPIRE = Integer.parseInt((String) Configuration.prop.get("solr.cache.expire.seconds"));
            CACHE_CONTROL_MAX_AGE = Integer.parseInt((String) Configuration.prop.get("cache.control.max.age.seconds"));
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
