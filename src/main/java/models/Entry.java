package models;

import java.io.IOException;

import app.DBConnection;
import app.MainScenesController;

/**
 * Creates entries in the database.
 * @author Daniel Casper
 * @version 7/9/24
 */
public class Entry {

    private String name;
    private String num;
    private Date date;
    private String publisher;
    private boolean xmen;
    private boolean xmenAdj;
    private MainScenesController mainController;

    /**
     * Constructor for an entry object that sets all instance variable values
     * @param name Name of a series (correlates to SeriesTitle column in Series table)
     * @param num Issue numbers/title (correlates to IssueName column in Comic table) 
     * Can contain a set of issues in the form of "firstIssue-lastIssue"
     * @param date Date for the entry (correlates to dateString column in Comic table) 
     * @param publisher Name of a series publisher (correlates to Publisher column in Series table) 
     * @param xmen Boolean indicating whether or not the series is an x-men series (correlates to xmen column in Series table) 
     * @param xmenAdj Boolean indicating whether or not an entry contains x-men adjacent issues 
     * (if true, will add series and issue to xmenadj tables) 
     * @throws IOException
     */
    public Entry(String name, String num, Date date, String publisher, boolean xmen, boolean xmenAdj, MainScenesController main) throws IOException{
        this.mainController=main;
        this.name=name;
        this.num=num;
        this.date=date;
        this.publisher=publisher;
        this.xmen=xmen;
        this.xmenAdj=xmenAdj;
    }
    
    /**
     * Creates a new entry in the database, creating a new series if needed and adding all indicated issues
     * @return true if entry made successfully, false if error message is displayed
     */
    public boolean makeEntry(){
        int bookID=DBConnection.getSeriesIDByTitle(name);
        if (bookID!=0){
            addBook(bookID);
            return true;
        }
        else{
            bookID=DBConnection.createSeries(name, publisher, xmen); 
            if (publisher.equals("")){
                mainController.errorMessage("No Publisher Selected", "Please Select a Publisher");
                return false;
            }
            else{
                addBook(bookID);
                return true;
            }
            }
        }

    /**
     * Adds either a set of several issues or a single issue as new rows in the Comic table
     * @param run integer ID for a series (correlates to SeriesID in all tables in database)
     */
    private void addBook(int run){
        if (num.contains("-") && num.split("-")[0]!=""){
            String[] issues=num.split("-");
            int issue=Integer.parseInt(issues[0]);
            while (issue<=Integer.parseInt(issues[1])){
                addIssue(run,Integer.toString(issue));
                issue++;
            }
        }
        else{
            addIssue(run,num);
        }
    }

    /**
     * Adds an individual issue of a comic to the database as a new row in the Comic table
     * @param run integer ID for a series (correlates to SeriesID in all tables in database)
     */
    private void addIssue(int run, String issue){
        if (!DBConnection.entryExists(run,issue,date.toString(),date.getMonth(),date.getDay(),date.getYear())){
            DBConnection.addIssue(run,issue,date.toString(),date.getMonth(),date.getDay(),date.getYear(),xmenAdj);
        }
        else{
            mainController.errorMessage("Entry Exists", "This Entry Exists");
        }
    }

}