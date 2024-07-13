package app.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import app.DBConnection;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Entry;

/**
 * Controller for making a new entry
 *
 * @author Daniel Casper
 */
public class MakeEntryDialogController {
    private ArrayList<String> months;
    private CheckBox xmen;
    private CheckBox xmenAdj;
    private MenuButton year;
    @FXML private AnchorPane pane;
    @FXML private MenuButton month;
    @FXML private MenuButton publisher;
    @FXML private MenuButton seriesTitles;


    @SuppressWarnings("exports")
    @FXML public TextField seriesField;
    @SuppressWarnings("exports")
    @FXML public TextField dateField;
    @SuppressWarnings("exports")
    @FXML public TextField issuesField; 
    @FXML private Label TotalValue;
    @FXML private Label marvelTotalValue;
    @FXML private Label dcTotalValue;
    @FXML private Label imageTotalValue;
    @FXML private Label xmenTotalValue;
    @FXML private Label seriesTotal;
    @FXML private Label seriesTotalValue;

    //private Authenticator authenticator;

    public void setObjects() {
        months=new ArrayList<String>(Arrays.asList("Overview","Yearly","January","February","March","April","May","June","July","August","September","October","November","December"));
        xmen=new CheckBox("X-Men?");
        xmenAdj=new CheckBox("X-Men Adjacent?");
        seriesField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent t) {
                makeTitlesButton();
            }
        }); 
        xmen.setLayoutX(100);
        xmen.setVisible(false);
        xmenAdj.setLayoutX(100);
        xmenAdj.setLayoutY(30);
        xmenAdj.setVisible(false);
        year=new MenuButton("");
        year.setLayoutX(350);
        year.setVisible(false);
        updateView();
        pane.getChildren().add(1, xmen);
        pane.getChildren().add(1, xmenAdj);
        pane.getChildren().add(1, year);
        makeYearButton();
        makeMonthsButton();
        makePublisherButton();
        makeTitlesButton();
    }

    /**
     * Called when login as student is clicked
     * @param mouseEvent
     * @throws IOException 
     */
    public void makeEntry(@SuppressWarnings("exports") MouseEvent mouseEvent) throws IOException {
        if (seriesField.getText().equals("") || issuesField.getText().equals("") || dateField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fields Empty");
            alert.setHeaderText(null);
            alert.setContentText("Please Fill Out All Fields!");
            alert.showAndWait();
        }
        else if (issuesField.getText().contains(",")){
            String[] issues=issuesField.getText().split(",");
            String[] dates=dateField.getText().split(",");
            if (issues.length!=dates.length){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Improper Entry");
                alert.setHeaderText(null);
                alert.setContentText("Please ensure each set of issues has a correspondig date!");
                alert.showAndWait();
            }
            else{
                String publisherStr=publisher.getText();
                String seriesName=seriesField.getText();
                boolean isXmen=xmen.isSelected();
                boolean isXmenAdj=xmenAdj.isSelected();
                for (int i=0;i<issues.length;i++){
                    if (properDate(dates[i])){
                        Entry entry=new Entry(seriesName, issues[i], dates[i], publisherStr, isXmen,isXmenAdj);
                        if (!entry.makeEntry()){
                            i=issues.length;
                        }
                        updateView();           
                    }     
                }
            }
        }
        else{
            Entry entry=new Entry(seriesField.getText(), issuesField.getText(), dateField.getText(), publisher.getText(), xmen.isSelected(),xmenAdj.isSelected());
            entry.makeEntry();
            updateView();
        }
    }

    public void makePublisherButton(){
        ObservableList<MenuItem> publishers=publisher.getItems();
        MenuItem item1=new MenuItem("DC");
        MenuItem item2=new MenuItem("Marvel");
        MenuItem item3=new MenuItem("Image");
        item1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                publisher.setText(item1.getText());
                xmen.setVisible(false);
                xmen.setSelected(false);
                xmenAdj.setVisible(false);
                xmenAdj.setSelected(false);
            }
        });
        item2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                publisher.setText(item2.getText());
                xmen.setVisible(true);
                xmenAdj.setVisible(true);
            }
        });
        item3.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                publisher.setText(item3.getText());
                xmen.setVisible(false);
                xmen.setSelected(false);
                xmenAdj.setVisible(false);
                xmenAdj.setSelected(false);
            }
        });
        publishers.add(item1);
        publishers.add(item2);
        publishers.add(item3);
    }

    public void updateView(){
        int monthInt=months.indexOf(month.getText())-1;
        publisher.setText("");
        xmen.setVisible(false);
        xmen.setSelected(false);
        xmenAdj.setVisible(false);
        xmenAdj.setSelected(false);
        if (month.getText().equals("") || month.getText().equals("Overview")){
            setStatValues(DBConnection.getTotal(), DBConnection.getNumXMen(), DBConnection.getNumPublisher("DC"), DBConnection.getNumPublisher("Marvel"), DBConnection.getNumSeries(), DBConnection.getNumPublisher("Image"));
            year.setVisible(false);
            year.setText("");
        }
        else if (month.getText().equals("Yearly")){
            int dcSum=0;
            int xmenSum=0;
            int marvelSum=0;
            int imageSum=0;
            int seriesSum=0;
            String yearStr=year.getText().split("0")[1];
            TotalValue.setText(Integer.toString(DBConnection.getTotalYear(Integer.parseInt(yearStr))));
            int idMax=DBConnection.getNumSeries()+1;
            for (int i=1;i<=idMax;i++){
                String publisher=DBConnection.getPublisherByID(i);
                int addTo=DBConnection.getNumByYear(i,Integer.parseInt(yearStr));
                switch(publisher){
                    case "Marvel": marvelSum+=addTo; break; 
                    case "Image": imageSum+=addTo; break;
                    case "DC": dcSum+=addTo; break;
                }
                if (addTo>0){
                    seriesSum++;
                }  
            }
            ArrayList<Integer> list=DBConnection.getXmen();
            int x=0;
            while (x<list.size()){
                xmenSum+=DBConnection.getNumByYear(list.get(x),Integer.parseInt(yearStr));
                x++;
            }
            list=DBConnection.getXmenAdj();
            x=0;
            while (x<list.size()){
                xmenSum+=DBConnection.getNumXmenByYear(list.get(x),Integer.parseInt(yearStr));
                x++;
            }
            setStatValues((marvelSum+dcSum+imageSum), xmenSum, dcSum, marvelSum, seriesSum, imageSum);
        }
        else{
            int dcSum=0;
            int xmenSum=0;
            int marvelSum=0;
            int imageSum=0;
            int seriesSum=0;
            String yearStr=year.getText().split("0")[1];
            TotalValue.setText(Integer.toString(DBConnection.getTotalMonth(monthInt,Integer.parseInt(yearStr))));
            int idMax=DBConnection.getNumSeries()+10;
            for (int i=1;i<=idMax;i++){
                String publisher=DBConnection.getPublisherByID(i);
                int addTo=DBConnection.getNumByMonth(i,monthInt,Integer.parseInt(yearStr));
                switch(publisher){
                    case "Marvel": marvelSum+=addTo; break; 
                    case "Image": imageSum+=addTo; break;
                    case "DC": dcSum+=addTo; break;
                }
                if (addTo>0){
                    seriesSum++;
                }  
            }
            ArrayList<Integer> list=DBConnection.getXmen();
            int x=0;
            while (x<list.size()){
                xmenSum+=DBConnection.getNumByMonth(list.get(x),monthInt,Integer.parseInt(yearStr));
                x++;
            }
            list=DBConnection.getXmenAdj();
            x=0;
            while (x<list.size()){
                xmenSum+=DBConnection.getNumXmenByMonth(list.get(x),monthInt,Integer.parseInt(yearStr));
                x++;
            }
            setStatValues((marvelSum+dcSum+imageSum), xmenSum, dcSum, marvelSum, seriesSum, imageSum);
        }
        seriesField.setText("");
        issuesField.setText("");
        dateField.setText("");
        seriesTitles.setText("");
        makeTitlesButton();
    }

    private void setStatValues(int total, int xmen, int dc, int marvel, int series, int image){
        TotalValue.setText(Integer.toString(total));
        dcTotalValue.setText(Integer.toString(dc));                   
        imageTotalValue.setText(Integer.toString(image)); 
        xmenTotalValue.setText(Integer.toString(xmen));
        marvelTotalValue.setText(Integer.toString(marvel));
        seriesTotalValue.setText(Integer.toString(series));
    }

    public void makeTitlesButton(){
        ObservableList<MenuItem> bookNames=seriesTitles.getItems();
        bookNames.clear();
        MenuItem item1=new MenuItem("");
        item1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                seriesField.setText("");
                seriesTitles.setText("");
            }
        });
        bookNames.add(item1);
        ArrayList<String> titles=DBConnection.getSeries();
        for (int i=0;i<titles.size();i++){
            if (titles.get(i).contains(seriesField.getText())){
                MenuItem item=new MenuItem(titles.get(i));
                item.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        seriesField.setText(item.getText());
                        seriesTitles.setText(item.getText());
                    }
                });
                bookNames.add(item);
            }
        }
    }

    public void makeMonthsButton(){
        ObservableList<MenuItem> monthsItems=month.getItems();
        for (int i=0;i<months.size();i++){
            MenuItem item=new MenuItem(months.get(i));
            item.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    month.setText(item.getText());
                    year.setVisible(true);
                    if (!year.getText().equals("")){
                        updateView();
                    }
                }
            });
            monthsItems.add(item);
        }
    }

    public void makeYearButton(){
        ObservableList<MenuItem> years=year.getItems();
        MenuItem item1=new MenuItem("2022");
        MenuItem item2=new MenuItem("2023");
        MenuItem item3=new MenuItem("2024");
        item1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                year.setText(item1.getText());
                updateView();
            }
        });
        item2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                year.setText(item2.getText());
                updateView();
            }
        });
        item3.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                year.setText(item3.getText());
                updateView();
            }
        });
        years.add(item1);
        years.add(item2);
        years.add(item3);
    }

    public boolean properDate(String dateString){
        if (dateString.split("/").length!=3){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fields Empty");
            alert.setHeaderText(null);
            alert.setContentText("Please Properly Enter Date!");
            alert.showAndWait();
            return false;
        }
        return true;
    }
}