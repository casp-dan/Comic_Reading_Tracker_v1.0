package models;

import java.util.ArrayList;

/**
 * Creates entries in the database.
 * @author Daniel Casper
 * @version 7/9/24
 */
public class Entry {

    private String seriesName;
    private ArrayList<String> issues;
    private Date date;
    private String publisher;
    private boolean xmen;
    private boolean xmenAdj;

    /**
     * Constructor for an entry object that sets all instance variable values
     * @param seriesName Name of a series (correlates to SeriesTitle column in Series table)
     * @param issueName Issue numbers/title (correlates to IssueName column in Comic table)
     * Can contain a set of issues in the form of "firstIssue-lastIssue"
     * @param date Date for the entry (correlates to dateString column in Comic table) 
     * @param publisher Name of a series publisher (correlates to Publisher column in Series table) 
     * @param xmen Boolean indicating whether or not the series is an x-men series (correlates to xmen column in Series table)
     * @param xmenAdj Boolean indicating whether or not an entry contains x-men adjacent issues 
     * (if true, will add series and issue to xmenadj tables) 
     */
    public Entry(String seriesName, String issueName, Date date, String publisher, boolean xmen, boolean xmenAdj) {
        this.seriesName=seriesName;
        this.issues=makeIssueList(issueName);
        this.date=date;
        this.publisher=publisher;
        this.xmen=xmen;
        this.xmenAdj=xmenAdj;
    }
    
    /**
     * Getter for the name variable
     * @return string of the name
     */
    public String getSeriesName(){
        return seriesName;
    }
    
    /**
     * Getter for the num variable
     * @return list of the issues
     */
    public ArrayList<String> getIssues(){
        return issues;
    }
    
    /**
     * Getter for the date variable
     * @return string of the date
     */
    public Date getDate(){
        return date;
    }
    
    /**
     * Getter for the publisher variable
     * @return string of the publisher
     */
    public String getPublisher(){
        return publisher;
    }
    
    /**
     * Getter for the xmen variable
     * @return boolean value of xmen variable
     */
    public boolean getXmen(){
        return xmen;
    }
    
    /**
     * Getter for the xmenAdj variable
     * @return boolean value of xmenAdj variable
     */
    public boolean getXmenAdj(){
        return xmenAdj;
    }

    public boolean equals(Entry otherEntry){
        if (!seriesName.equals(otherEntry.getSeriesName())){
            return false;
        }
        else if (!issues.equals(otherEntry.getIssues())){
            return false;
        }
        else if (!date.equals(otherEntry.getDate())){
            return false; 
        }
        else if (!publisher.equals(otherEntry.getPublisher())){
            return false;
        }
        else if (xmen && !otherEntry.getXmen()){
            return false;
        }
        else return !xmenAdj || otherEntry.getXmenAdj();
    }

    /**
     * Create an array list of all the issue names for the entry
     * @param issueName the string that was provided as the input 
     * for the issues text field of the controller
     * @return an ArrayList containing the names of all the issues
     * for the entry in order
     */
    private ArrayList<String> makeIssueList(String issueName){
        if (issueName.contains("-") && !issueName.split("-")[0].isEmpty()){
            String[] issuesList=issueName.split("-");
            int issue=Integer.parseInt(issuesList[0]);
            int issueEnd=Integer.parseInt(issuesList[1]);
            issues= new ArrayList<>();
            while (issue<=issueEnd){
                issues.add(Integer.toString(issue));
                issue++;
            }
        }
        else{
            issues= new ArrayList<>();
            issues.add(issueName);
        }
        return issues;
    }

}