package model;

import java.util.ArrayList;

import app.DBConnection;

import java.io.IOException;
import javafx.scene.control.Alert;


public class Entry {

    public Entry(String name, String num, String publisher, ArrayList<Book> list, boolean xmen) throws IOException{
        int bookID=isBook(name);
        if (bookID!=0){
            addBook(bookID, list, num);
        }
        else{
            switch(publisher){
                case "Marvel":
                    bookID=DBConnection.createSeries(name, 0, "Marvel", xmen); 
                    if (num.contains("-")){
                        String[] issues=num.split("-");
                        int issue=Integer.parseInt(issues[0]);
                        while (issue<=Integer.parseInt(issues[1])){
                            DBConnection.addIssue(bookID,num,6);
                            issue++;
                        }
                    }
                    else{
                        DBConnection.addIssue(bookID,num,6);
                    }
                    break;
                case "DC": 
                    bookID=DBConnection.createSeries(name, 0, "DC", xmen); 
                    if (num.contains("-")){
                        String[] issues=num.split("-");
                        int issue=Integer.parseInt(issues[0]);
                        while (issue<=Integer.parseInt(issues[1])){
                            DBConnection.addIssue(bookID,num,6);
                            issue++;
                        }
                    }
                    else{
                        DBConnection.addIssue(bookID,num,6);
                    }
                    break;
                case "Image": 
                    bookID=DBConnection.createSeries(name, 0, "Image", xmen); 
                    if (num.contains("-")){
                        String[] issues=num.split("-");
                        int issue=Integer.parseInt(issues[0]);
                        while (issue<=Integer.parseInt(issues[1])){
                            DBConnection.addIssue(bookID,num,6);
                            issue++;
                        }
                    }
                    else{
                        DBConnection.addIssue(bookID,num,6);
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

    public void addBook(int run, ArrayList<Book> list, String num){
        if (num.contains("-")){
            String[] issues=num.split("-");
            int issue=Integer.parseInt(issues[0]);
            while (issue<=Integer.parseInt(issues[1])){
                DBConnection.addIssue(run,num,6);
                issue++;
            }
        }
        else{
            DBConnection.addIssue(run,num,6);
        }
    }

    public int isBook(String name){
        return DBConnection.getSeriesIDByTitle(name);
    }
}
