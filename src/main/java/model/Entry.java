package model;

import app.DBConnection;

import java.io.IOException;
import javafx.scene.control.Alert;


public class Entry {

    private String name;
    private String num;
    private String date;
    private String publisher;
    private boolean xmen;
    private boolean xmenAdj;
    private String[] comps;

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
    public Entry(String name, String num, String date, String publisher, boolean xmen, boolean xmenAdj) throws IOException{
        this.comps=date.split("/");
        if (comps[2].length()>2){
            comps[2]=comps[2].split("0")[1];
        }
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
            addBook(bookID,comps);
            return true;
        }
        else{
            bookID=DBConnection.createSeries(name, 0, publisher, xmen); 
            if (publisher.equals("")){
                errorMessage("No Publisher Selected", "Please Select a Publisher");
                return false;
            }
            else{
                addBook(bookID,comps);
                return true;
            }
            }
        }

    /**
     * Adds either a set of several issues or a single issue as new rows in the Comic table
     * @param run integer ID for a series (correlates to SeriesID in all tables in database)
     * @param comps array of the components of the date string 
     */
    private void addBook(int run, String[] comps){
        if (num.contains("-")){
            String[] issues=num.split("-");
            int issue=Integer.parseInt(issues[0]);
            while (issue<=Integer.parseInt(issues[1])){
                addIssue(run);
                issue++;
            }
        }
        else{
            addIssue(run);
        }
    }

    /**
     * Adds an individual issue of a comic to the database as a new row in the Comic table
     * @param run integer ID for a series (correlates to SeriesID in all tables in database)
     */
    private void addIssue(int run){
        if (!DBConnection.entryExists(run,num,date,Integer.parseInt(comps[0]),Integer.parseInt(comps[1]),Integer.parseInt(comps[2]))){
            DBConnection.addIssue(run,num,date,Integer.parseInt(comps[0]),Integer.parseInt(comps[1]),Integer.parseInt(comps[2]),xmenAdj);
        }
        else{
            errorMessage("Entry Exists", "This Entry Exists");
        }
    }

    /**
     * Creates and error message JavaFX alert with the given message.
     * @param title Title for the alert window
     * @param message Error message for the alert window
     */
    private void errorMessage(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}