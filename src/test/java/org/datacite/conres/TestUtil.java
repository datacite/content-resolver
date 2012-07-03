package org.datacite.conres;

import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;

public abstract class TestUtil {
    public static byte[] loadData(Class clazz, String fileName){
        InputStream is = clazz.getResourceAsStream(fileName);
        try {
            return ByteStreams.toByteArray(is);
        } catch (IOException e) {
            return null;
        }
    }
}
