package model;

import java.util.ArrayList;
import java.io.IOException;
import javafx.scene.control.Alert;


public class Entry {

    public Entry(String name, String num, String publisher, ArrayList<Book> list, boolean xmen) throws IOException{
        Book run=getBook(list,name);
        if (run!=null){
            addBook(run, list, num);
        }
        else{
            switch(publisher){
                case "Marvel": 
                    run=new MarvelBook(name, xmen);
                    addBook(run,list,num);
                    break;
                case "DC": 
                    run=new DCBook(name);
                    addBook(run,list,num);
                    break;
                case "Image": 
                    run=new ImageBook(name);
                    addBook(run,list,num);
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

    public void addBook(Book run, ArrayList<Book> list, String num){
        list.add(run);
        if (num.contains("-")){
            String[] issues=num.split("-");
            run.addIssue(Integer.parseInt(issues[0]),Integer.parseInt(issues[1]));
        }
        else{
            run.addIssue(Integer.valueOf(num));
        }
    }

    public Book getBook(ArrayList<Book> list, String name){
        for (Book series: list){
            if (series.getTitle().equals(name)){
                return series;
            }
        }
        return null;
    }
}
