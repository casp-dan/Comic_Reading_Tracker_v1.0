package models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Model for a date object containing the month, day, and year
 * @author Daniel Casper
 */

public class Date {

    private int day;
    private int month;
    private int year;

    private String START="1/1/22";
    private ArrayList<Integer> THIRTYONE=new ArrayList<Integer>(Arrays.asList(1,3,5,7,8,10,12));
    private ArrayList<Integer> THIRTY=new ArrayList<Integer>(Arrays.asList(4,6,9,11));

    /**
     * Default constructor
     * @param date input date
     */
    public Date(String date){
        String[] comps=breakdown(date);
        if (validate(comps)){
            month=Integer.parseInt(comps[0]);
            day=Integer.parseInt(comps[1]);
            year=Integer.parseInt(comps[2]);
            if (!realDate()){
                month=0;
                day=0;
                year=0;
            }
        }
    }

    /**
     * Constructor for the current date as determined by the LocalDate module
     * @param today the input date
     */
    public Date(LocalDate today){
        String[] comps=today.toString().split("-");
        if (Integer.parseInt(comps[1])<10){
            month=Integer.parseInt(comps[1].split("0")[1]);
        }
        else{
            month=Integer.parseInt(comps[1]);
        }
        if (Integer.parseInt(comps[2])<10){
            day=Integer.parseInt(comps[2].split("0")[1]);
        }
        else{
            day=Integer.parseInt(comps[2]);
        }
        year=Integer.parseInt(comps[0].split("0")[1]);
    }

    /**
     * Getter for the day
     * @return integer of the day variable
     */
    public int getDay(){
        return day;
    }

    /**
     * Getter for the month
     * @return integer of the month variable
     */
    public int getMonth(){
        return month;
    }

    /**
     * Getter for the year
     * @return integer of the year variable
     */
    public int getYear(){
        return year;
    }

    /**
     * Returns a string representation of the date in the form of mm/dd/yy
     * (single digit numbers will be # rather than 0#)
     */
    public String toString(){
        return Integer.toString(month)+"/"+Integer.toString(day)+"/"+Integer.toString(year);
    }

    /**
     * Checks if the date falls between the constant start 
     * date and the current date according to the LocalDate module.
     * @return true if the date falls within the acceptable range,
     * false if not.
     */
    public boolean withinRange(){
        boolean validDate=false;
        Date start=new Date(START);
        Date today=new Date(LocalDate.now());
        if (!tooEarly(start)){
            if (!tooLate(today)){
                validDate=true;
            }
        }
        return validDate;
    }
    
    public int compareTo(Date otherDate) {
        if (tooEarly(otherDate)){
            return -1;
        }
        else if (tooLate(otherDate)){
            return 1;
        }
        else{
            return 0;
        }
    }
    
    public boolean equals(Date otherDate) {
        if (toString().equals(otherDate.toString())){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Checks if the date is from before 
     * the constant start date
     * @param start date object of the earliest 
     * acceptable date
     * @return true if the date comes before the 
     * constant start date, false if not
     */
    private boolean tooEarly(Date start){
        boolean tooEarly=false;
        if (year>=start.getYear()){
            if (year==start.getYear()){
                if (month<start.getMonth()){
                    tooEarly=true;
                }
                else if (day<start.getDay()){               
                    tooEarly=true;
                }
                else{
                    tooEarly=false;
                }
            }
        }
        else{
            tooEarly=true;
        }
        return tooEarly;
    }

    /**
     * Checks if the date is from after 
     * the current date according to the 
     * LocalDate module
     * @return true if the date comes after the 
     * current date, false if not
     */
    private boolean tooLate(Date today){
        boolean tooLate=false;
        if (year==today.getYear()){
            if (month>today.getMonth()){
                tooLate=true;
            }
            else if (day>today.getDay()){               
                tooLate=true;
            }
            else{
                tooLate=false;
            }
        }
        else if (year>today.getYear()){
            tooLate=true;
        }
        else{
            tooLate=false;
        }
        return tooLate;
    }

    /**
     * Checks if the input string is all numbers 
     * and there is an input for the month, day, 
     * and year.
     * @param comps array of strings for the date in the form of
     * mm/dd/yy (single digit numbers will be # rather than 0#)
     * @return true if the input is valid, false if not
     */
    private boolean validate(String[] comps){
        boolean valid=true;
        int i=0;
        if (comps!=null){
            Integer[] numComps=new Integer[comps.length];
            while (i<comps.length){
                try{
                    numComps[i]=Integer.parseInt(comps[i]);
                }catch(NumberFormatException e){
                    valid=false;
                }
                i++;
            }
        }
        else{
            valid=false;
        }
        return valid;
    }

    /**
     * Checks if the given date exists on a calendar
     * @return true if the date exists on the calendar,
     * false if not.
     */
    private boolean realDate(){
        boolean real=true;
        if (month>12){
            real=false;
        }
        else if (month==2){
            real=leapYear();
        }
        else if (THIRTYONE.contains(month)){
            if (day>31){
                real=false;
            }
        }
        else if (THIRTY.contains(month)){
            if (day>30){
                real=false;
            }
        }
        return real;
    }

    /**
     * Checks if the given year is a leap year and whether 
     * or not the day is a real date for that year
     * @return true if the day exists on a given year, 
     * and false if not
     */
    private boolean leapYear(){
        if (year%4==0){
            if (day>29){
                return false;
            }
        }
        else{
            if (day>28){
                return false;
            }
        }
        return true;
    }

    /**
     * Breaks down the input string into an array with the 
     * month, day, and year with formatting.
     * @param date input string to the constructor
     * @return array of strings for the date in the form of
     * mm/dd/yy (single digit numbers will be # rather than 0#)
     */
    private String[] breakdown(String date){
        String[] comps=date.split("/");
        if (comps.length==3){
            if (comps[2].length()>2){
                comps[2]=comps[2].split("0")[1];
            }
        }
        else{
            comps=null;
        }
        return comps;
    }

}
