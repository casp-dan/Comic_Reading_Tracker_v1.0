package model;

import app.DBConnection;

import java.io.IOException;
import javafx.scene.control.Alert;


public class Entry {

    public Entry(String name, String num, String date, String publisher, boolean xmen) throws IOException{
        int dateID=DBConnection.getDateByString(date);
        if (dateID==0){
            dateID=getDate(date);
        }
        int bookID=DBConnection.getSeriesIDByTitle(name);
        if (bookID!=0){
            addBook(bookID, num, dateID);
        }
        else{
            switch(publisher){
                case "Marvel":
                    bookID=DBConnection.createSeries(name, 0, "Marvel", xmen); 
                    if (num.contains("-")){
                        String[] issues=num.split("-");
                        int issue=Integer.parseInt(issues[0]);
                        while (issue<=Integer.parseInt(issues[1])){
                            if (!DBConnection.entryExists(bookID,Integer.toString(issue),dateID)){
                                DBConnection.addIssue(bookID,Integer.toString(issue),dateID);
                                issue++;
                            }
                            else{
                                comicExistsError();
                            }
                        }
                    }
                    else{
                        if (!DBConnection.entryExists(bookID,num,dateID)){
                            DBConnection.addIssue(bookID,num,dateID);
                        }
                        else{
                            comicExistsError();
                        }
                    }
                    break;
                case "DC": 
                    bookID=DBConnection.createSeries(name, 0, "DC", xmen); 
                    if (num.contains("-")){
                        String[] issues=num.split("-");
                        int issue=Integer.parseInt(issues[0]);
                        while (issue<=Integer.parseInt(issues[1])){
                            if (!DBConnection.entryExists(bookID,Integer.toString(issue),dateID)){
                                DBConnection.addIssue(bookID,Integer.toString(issue),dateID);
                                issue++;
                            }
                            else{
                                comicExistsError();
                            }
                        }
                    }
                    else{
                        if (!DBConnection.entryExists(bookID,num,dateID)){
                            DBConnection.addIssue(bookID,num,dateID);
                        }
                        else{
                            comicExistsError();
                        }
                    }
                    break;
                case "Image": 
                    bookID=DBConnection.createSeries(name, 0, "Image", xmen); 
                    if (num.contains("-")){
                        String[] issues=num.split("-");
                        int issue=Integer.parseInt(issues[0]);
                        while (issue<=Integer.parseInt(issues[1])){
                            if (!DBConnection.entryExists(bookID,Integer.toString(issue),dateID)){
                                DBConnection.addIssue(bookID,Integer.toString(issue),dateID);
                                issue++;
                            }
                            else{
                                comicExistsError();
                            }
                        }
                    }
                    else{
                        if (!DBConnection.entryExists(bookID,num,dateID)){
                            DBConnection.addIssue(bookID,num,dateID);
                        }
                        else{
                            comicExistsError();
                        }
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

    public void addBook(int run, String num, int dateID){
        if (num.contains("-")){
            String[] issues=num.split("-");
            int issue=Integer.parseInt(issues[0]);
            while (issue<=Integer.parseInt(issues[1])){
                if (!DBConnection.entryExists(run,Integer.toString(issue),dateID)){
                    DBConnection.addIssue(run,Integer.toString(issue),dateID);
                    issue++;
                }
                else{
                    comicExistsError();
                }
            }
        }
        else{
            if (!DBConnection.entryExists(run,num,dateID)){
                DBConnection.addIssue(run,num,dateID);
            }
            else{
                comicExistsError();
            }
        }
    }

    public int getDate(String date){
        String[] comps=date.split("/");
        return DBConnection.newDate(date,Integer.parseInt(comps[0]),Integer.parseInt(comps[1]),Integer.parseInt(comps[2])); 
    }

    public void comicExistsError(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("No Publisher Selected");
        alert.setHeaderText(null);
        alert.setContentText("This Entry Exists");
        alert.showAndWait();
    }

}
