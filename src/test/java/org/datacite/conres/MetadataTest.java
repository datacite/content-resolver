package org.datacite.conres;

import org.junit.*;

import javax.ws.rs.core.MediaType;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MetadataTest {
    public String loadData(String fileName){
        InputStream is = getClass().getResourceAsStream(fileName);
        Scanner scanner = new Scanner(is, "UTF-8");
        StringBuilder sb = new StringBuilder();
        while(scanner.hasNextLine())
            sb.append(scanner.nextLine());

        return sb.toString();
    }
    
    @Test
    public void parsingTest1(){
        String xml = loadData("/test1.xml");
        assertNotNull(xml);
        assertTrue(xml.length() != 0);
        Map<MediaType,URI> media = new HashMap<MediaType, URI>();
        Metadata m = new Metadata(MockSearchServiceImpl.TEST_DOI, xml, media);
        assertEquals(1, m.getResourceType().size());
    }
}
