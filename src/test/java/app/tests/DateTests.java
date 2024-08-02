/**
 * Tests for the Classroom Class
 *
 * @author: Guy Tallent
 * @version: 6/1/2023
 */


package app.tests;


import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.Month;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import models.Date;


@RunWith(JUnit4.class)

public class DateTests {

    private Date d;
    private String dStr="1/1/23";
    private int day;
    private int month;
    private int year;


    @Before
    public void Setup(){
        d = new Date(dStr);
        day=1;
        month=1;
        year=23;
    }

    @After
    public void tearDown(){
        d=null; 
    }

    @Test
    public void getDay(){
        assertEquals(day,d.getDay());
    }
    
    @Test
    public void getMonth(){
        assertEquals(month,d.getMonth());
    }
    
    @Test
    public void getYear(){
        assertEquals(year,d.getYear());
    }

    @Test
    public void withinRange(){
        Date tooEarly=new Date("1/1/20");
        Date tooLate=new Date("1/1/99");
        assertTrue("Using a valid date returns true for withinRange method", d.withinRange());
        assertFalse("Using an invalid date that is too early returns false for withinRange method", tooEarly.withinRange());
        assertFalse("Using an invalid date that is too early returns false for withinRange method", tooLate.withinRange());
    }

    @Test
    public void constructorTest(){
        Date date;
        assertEquals("Test constructor with a valid date",dStr,d.toString());
        date=new Date("1/2/");
        assertEquals("Test constructor with only month and date","0/0/0",date.toString());
        date=new Date("/2/23");
        assertEquals("Test constructor with only day and year","0/0/0",date.toString());
        date=new Date("1//23");
        assertEquals("Test constructor with only month and year","0/0/0",date.toString());
        date=new Date("1/2/w");
        assertEquals("Test constructor with non-integer in year","0/0/0",date.toString());
        date=new Date("w/2/23");
        assertEquals("Test constructor with non-integer in month","0/0/0",date.toString());
        date=new Date("1/w/23");
        assertEquals("Test constructor with non-integer in day","0/0/0",date.toString());
        date=new Date("");
        assertEquals("Test constructor with empty input","0/0/0",date.toString());
        Month jan=Month.of(1);
        LocalDate today=LocalDate.of(2023,jan,1);
        date=new Date(today);
        assertEquals("Test LocalDate constructor with a valid date",dStr,date.toString());
    }

}
