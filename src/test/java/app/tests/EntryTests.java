/**
 * Tests for the Group Class
 *
 * @author: Guy Tallent
 * @version: 6/1/2023
 */


package app.tests;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.mockito.Mockito;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


import models.*;



@RunWith(JUnit4.class)

public class EntryTests {

    //private Student s;
    //private Group g;


    @Before
    public void Setup(){
        //s = Mockito.mock(Student.class);
        //g = new Group(s);

    }


    @After
    public void tearDown(){
        //g = null;
        //s =null;
    }


    @Test
    public void addStudent(){
        //g.addStudent(new Student("student1",0));
//
//
        //assertEquals(1, g.getSize());
    }


    @Test
    public void getStudent(){
        //g.addStudent(new Student("student1",0));
//
        //Mockito.when(s.getName()).thenReturn("student1");
//
        //assertEquals("student1",g.getStudent(0).getName());
    }


    @Test
    public void deleteStudent(){
        //g.addStudent(new Student("student1",0));
//
        //assertEquals(1, g.getSize());
//
        //g.deleteStudent(0);
//
        //assertEquals(0, g.getSize());
    }

    @Test
    public void getSize(){
        //g.addStudent(new Student("student1",0));
        //g.addStudent(new Student("student2",1));
        //g.addStudent(new Student("student3",2));
        //g.addStudent(new Student("student4",3));
//
        //assertEquals(4, g.getSize());
    }

    @Test
    public void getName(){
        //g.setName("group1");
        //assertEquals("group1",g.getName());
    }

    @Test
    public void getId(){
        //assertEquals(0, g.getID());
    }


}

