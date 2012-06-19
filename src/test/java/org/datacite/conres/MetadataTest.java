package org.datacite.conres;

import org.datacite.conres.model.Metadata;
import org.datacite.conres.service.impl.MockSearchServiceImpl;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MetadataTest {

    @Test
    public void parsingTest1(){
        byte[] xml = TestUtil.loadData(getClass(), "/test1.xml");
        assertNotNull(xml);
        assertTrue(xml.length != 0);
        Map<MediaType,URI> media = new HashMap<MediaType, URI>();
        Metadata m = new Metadata(MockSearchServiceImpl.TEST_DOI, xml, media, null, null, null, null, null, null);
        assertEquals(3, m.getCreators().size());
        assertEquals(1, m.getTitles().size());
        assertEquals("Radiolaria abundance of Hole 24-232A", m.getTitles().get(0).getValue());
        assertTrue(m.getPublisher().startsWith("PANGAEA"));
        assertEquals("2005", m.getPublicationYear());
        assertEquals(32, m.getSubjects().size());
        assertEquals("Leg24", m.getSubjects().get(29).getValue());
        assertEquals("eng", m.getLanguage());
        assertEquals(1, m.getResourceTypes().size());
        assertEquals("Dataset", m.getResourceTypes().get(0).getKey());
        assertEquals("Dataset", m.getResourceTypes().get(0).getValue());
        assertEquals("Cites", m.getRelatedIdentifiers().get(0).getKey());
        assertEquals("doi:10.2973/dsdp.proc.24.1974", m.getRelatedIdentifiers().get(0).getValue());
        assertTrue(m.getSizes().get(0).startsWith("204"));
        assertEquals("text/tab-separated-values", m.getFormats().get(0));
    }

    @Test
    public void parsingTest2(){
        byte[] xml = TestUtil.loadData(getClass(), "/test2.xml");
        assertNotNull(xml);
        assertTrue(xml.length != 0);
        Map<MediaType,URI> media = new HashMap<MediaType, URI>();
        Metadata m = new Metadata(MockSearchServiceImpl.TEST_DOI, xml, media, null, null, null, null, null, null);
        assertEquals(1, m.getResourceTypes().size());
        assertEquals("Text", m.getResourceTypes().get(0).getKey());
        assertEquals("Report", m.getResourceTypes().get(0).getValue());
        assertEquals("IsPartOf", m.getRelatedIdentifiers().get(0).getKey());
        assertEquals("doi:10.5284/1000328", m.getRelatedIdentifiers().get(0).getValue());
        assertEquals("ADS Grey Lit ID", m.getAlternateIdentifiers().get(0).getKey());
        assertEquals("12824", m.getAlternateIdentifiers().get(0).getValue());
        assertEquals("OASIS ID", m.getAlternateIdentifiers().get(1).getKey());
        assertEquals("universi1-17791", m.getAlternateIdentifiers().get(1).getValue());
        assertTrue(m.getSizes().get(0).startsWith("1"));
        assertEquals("PDF", m.getFormats().get(0));
        assertTrue(m.getRights().startsWith("http"));
        assertEquals("Other", m.getDescriptions().get(0).getKey());
        assertTrue(m.getDescriptions().get(0).getValue().startsWith("A4"));
    }

    @Test
    public void parsingTest3(){
        byte[] xml = TestUtil.loadData(getClass(), "/test3.xml");
        assertNotNull(xml);
        assertTrue(xml.length != 0);
        Map<MediaType,URI> media = new HashMap<MediaType, URI>();
        Metadata m = new Metadata(MockSearchServiceImpl.TEST_DOI, xml, media, null, null, null, null, null, null);
        assertTrue(m.getTitles().get(0).getValue().startsWith("Diabetes"));
        assertEquals(2, m.getDescriptions().size());
        assertTrue(m.getDescriptions().get(0).getValue().length() != 0);
        assertTrue(m.getDescriptions().get(1).getValue().length() != 0);
        assertEquals("Available", m.getDates().get(0).getKey());
        assertEquals("2011-11-09", m.getDates().get(0).getValue());
    }
    
    @Test
    public void parsingTest4(){
        byte[] xml = TestUtil.loadData(getClass(), "/test4.xml");
        assertNotNull(xml);
        assertTrue(xml.length != 0);
        Map<MediaType,URI> media = new HashMap<MediaType, URI>();
        Metadata m = new Metadata(MockSearchServiceImpl.TEST_DOI, xml, media, null, null, null, null, null, null);
        assertEquals("HostingInstitution", m.getContributors().get(0).getKey());
        assertTrue(m.getContributors().get(0).getValue().startsWith("Institute"));
        assertEquals("1.0", m.getVersion());
    }
}
