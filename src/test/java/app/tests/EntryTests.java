/**
 * Tests for the Group Class
 *
 * @author: Guy Tallent
 * @version: 6/1/2023
 */


package app.tests;


import static org.junit.Assert.assertEquals;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


import models.*;



@RunWith(JUnit4.class)

public class EntryTests {

    private Entry entry;

    @Before
    public void Setup(){
        entry=new Entry("title", "1", new Date("1/1/23"), "test", true, false);
    }


    @After
    public void tearDown(){
        entry=null;
    }


    @Test
    public void getSeriesName(){
        assertEquals("Test getSeriesName method", "title", entry.getSeriesName());
    }

    @Test
    public void getIssues(){
        ArrayList<String> issues=new ArrayList<String>();
        issues.add("1");
        assertEquals("Test getIssues method", issues, entry.getIssues());
    }

    @Test
    public void getDate(){
        Date date=new Date("1/1/23");
        assertEquals("Test getDate method", true, entry.getDate().equals(date));
    }

    @Test
    public void getPublisher(){
        assertEquals("Test getPublisher method", "test", entry.getPublisher());
    }

    @Test
    public void getXmen(){
        assertEquals("Test getXmen method", true, entry.getXmen());
    }

    @Test
    public void getXmenAdj(){
        assertEquals("Test getXmenAdj method", false, entry.getXmenAdj());
    }

    @Test
    public void equalsTest(){
        Entry newEntry=new Entry("title", "1", new Date("1/1/23"), "test", true, false);
        assertEquals("Test equals method with duplicate entry", true, entry.equals(newEntry));
    }
    @Test
    public void equalsTestWrongTitle(){
        Entry newEntry=new Entry("wrongTitle", "1", new Date("1/1/23"), "test", true, false);
        assertEquals("Test equals method with duplicate entry but wrong seriesName", false, entry.equals(newEntry));
    }
        
    @Test
    public void equalsTestWrongIssue(){
        Entry newEntry=new Entry("title", "2", new Date("1/1/23"), "test", true, false);
        assertEquals("Test equals method with duplicate entry but wrong issueName", false, entry.equals(newEntry));
    }
        
    @Test
    public void equalsTestWrongDate(){
        Entry newEntry=new Entry("title", "1", new Date("1/2/23"), "test", true, false);
        assertEquals("Test equals method with duplicate entry but wrong date", false, entry.equals(newEntry));
    }
        
    @Test
    public void equalsTestWrongPublisher(){
        Entry newEntry=new Entry("title", "1", new Date("1/1/23"), "wrongTest", true, false);
        assertEquals("Test equals method with duplicate entry but wrong publisher", false, entry.equals(newEntry));
    }
        
    @Test
    public void equalsTestWrongXmen(){
        Entry newEntry=new Entry("title", "1", new Date("1/1/23"), "test", false, false);
        assertEquals("Test equals method with duplicate entry but wrong xmen boolean", false, entry.equals(newEntry));
    }
        
    @Test
    public void equalsTestWrongXmenAdj(){
        Entry newEntry=new Entry("title", "1", new Date("1/1/23"), "test", false, true);
        assertEquals("Test equals method with duplicate entry but wrong xmenAdj boolean", false, entry.equals(newEntry));
    }       
}