package model;

import java.util.ArrayList;

import app.DBConnection;

import java.io.IOException;
import javafx.scene.control.Alert;


public class Entry {

    public Entry(String name, String num, String date, String publisher, ArrayList<Book> list, boolean xmen) throws IOException{
        int dateID=DBConnection.getDateByString(date);
        if (dateID==0){
            dateID=getDate(date);
        }
        int bookID=DBConnection.getSeriesIDByTitle(name);
        if (bookID!=0){
            addBook(bookID, list, num, dateID);
        }
        else{
            switch(publisher){
                case "Marvel":
                    bookID=DBConnection.createSeries(name, 0, "Marvel", xmen); 
                    if (num.contains("-")){
                        String[] issues=num.split("-");
                        int issue=Integer.parseInt(issues[0]);
                        while (issue<=Integer.parseInt(issues[1])){
                            DBConnection.addIssue(bookID,Integer.toString(issue),dateID);
                            issue++;
                        }
                    }
                    else{
                        DBConnection.addIssue(bookID,num,dateID);
                    }
                    break;
                case "DC": 
                    bookID=DBConnection.createSeries(name, 0, "DC", xmen); 
                    if (num.contains("-")){
                        String[] issues=num.split("-");
                        int issue=Integer.parseInt(issues[0]);
                        while (issue<=Integer.parseInt(issues[1])){
                            DBConnection.addIssue(bookID,Integer.toString(issue),dateID);
                            issue++;
                        }
                    }
                    else{
                        DBConnection.addIssue(bookID,num,dateID);
                    }
                    break;
                case "Image": 
                    bookID=DBConnection.createSeries(name, 0, "Image", xmen); 
                    if (num.contains("-")){
                        String[] issues=num.split("-");
                        int issue=Integer.parseInt(issues[0]);
                        while (issue<=Integer.parseInt(issues[1])){
                            DBConnection.addIssue(bookID,Integer.toString(issue),dateID);
                            issue++;
                        }
                    }
                    else{
                        DBConnection.addIssue(bookID,num,dateID);
                    }
                    break;
                default:
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("No Publisher Selected");
                    alert.setHeaderText(null);
                    alert.setContentText("Please Select a Publisher");
                    alert.showAndWait();
            };
        }
    }

    public void addBook(int run, ArrayList<Book> list, String num, int dateID){
        if (num.contains("-")){
            String[] issues=num.split("-");
            int issue=Integer.parseInt(issues[0]);
            while (issue<=Integer.parseInt(issues[1])){
                DBConnection.addIssue(run,Integer.toString(issue),dateID);
                issue++;
            }
        }
        else{
            DBConnection.addIssue(run,num,dateID);
        }
    }

    public int getDate(String date){
        String[] comps=date.split("/");
        return DBConnection.newDate(date,Integer.parseInt(comps[0]),Integer.parseInt(comps[1]),Integer.parseInt(comps[2])); 
    }

}
