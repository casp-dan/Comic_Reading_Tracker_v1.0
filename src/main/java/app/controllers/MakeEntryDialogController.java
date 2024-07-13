package app.controllers;

import model.*;
import javafx.fxml.FXML;
import app.DBConnection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.MenuButton;
import javafx.collections.ObservableList;

/**
 * Controller for making a new entry
 *
 * @author Daniel Casper
 */
public class MakeEntryDialogController {
    private ArrayList<String> months;
    private CheckBox xmen;
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
        months=new ArrayList<String>(Arrays.asList("Overview","January","February","March","April","May","June","July","August","September","October","November","December"));
        xmen=new CheckBox("X-Men?");
        xmen.setLayoutX(100);
        xmen.setVisible(false);
        year=new MenuButton("");
        year.setLayoutX(350);
        year.setVisible(false);
        updateView();
        pane.getChildren().add(1, xmen);
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
                for (int i=0;i<issues.length;i++){
                    if (properDate(dates[i])){
                        Entry entry=new Entry(seriesName, issues[i], dates[i], publisherStr, isXmen);
                        if (!entry.makeEntry()){
                            i=issues.length;
                        }
                        updateView();           
                    }     
                }
            }
        }
        else{
            Entry entry=new Entry(seriesField.getText(), issuesField.getText(), dateField.getText(), publisher.getText(), xmen.isSelected());
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
            }
        });
        item2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                publisher.setText(item2.getText());
                xmen.setVisible(true);
            }
        });
        item3.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                publisher.setText(item3.getText());
                xmen.setVisible(false);
                xmen.setSelected(false);
            }
        });
        publishers.add(item1);
        publishers.add(item2);
        publishers.add(item3);
    }

    public void updateView(){
        int monthInt=months.indexOf(month.getText());
        publisher.setText("");
        xmen.setVisible(false);
        xmen.setSelected(false);
        if (month.getText().equals("") || month.getText().equals("Overview")){
            TotalValue.setText(Integer.toString(DBConnection.getTotal()));
            xmenTotalValue.setText(Integer.toString(DBConnection.getNumXMen()));
            seriesTotalValue.setText(Integer.toString(DBConnection.getNumSeries()));
            dcTotalValue.setText(Integer.toString(DBConnection.getNumPublisher("DC")));
            imageTotalValue.setText(Integer.toString(DBConnection.getNumPublisher("Image")));
            marvelTotalValue.setText(Integer.toString(DBConnection.getNumPublisher("Marvel")));
        }
        else{
            int dcSum=0;
            int marvelSum=0;
            int imageSum=0;
            int xmenSum=0;
            int seriesSum=0;
            TotalValue.setText(Integer.toString(DBConnection.getTotalMonth(monthInt,Integer.parseInt(year.getText()))));
            int idMax=DBConnection.getNumSeries();
            String yearStr=year.getText().split("0")[1];
            for (int i=1;i<=idMax;i++){
                String publisher=DBConnection.getPublisherByID(i);
                if (publisher.equals("DC")){
                    int addTo=DBConnection.getNumByID(i,monthInt,Integer.parseInt(yearStr));
                    dcSum+=addTo;
                    if (addTo>0){
                        seriesSum++;
                    }
                }
                else if (publisher.equals("Image")){
                    int addTo=DBConnection.getNumByID(i,monthInt,Integer.parseInt(yearStr));
                    imageSum+=addTo;
                    if (addTo>0){
                        seriesSum++;
                    }                }
                else if (publisher.equals("Marvel")){
                    int addTo=DBConnection.getNumByID(i,monthInt,Integer.parseInt(yearStr));
                    marvelSum+=addTo;
                    if (DBConnection.isXmenByID(i)){
                        xmenSum+=DBConnection.getNumXmenByID(i,monthInt,Integer.parseInt(yearStr));
                    }
                    if (addTo>0){
                        seriesSum++;
                    }                
                }
            }
            dcTotalValue.setText(Integer.toString(dcSum));                   
            imageTotalValue.setText(Integer.toString(imageSum)); 
            xmenTotalValue.setText(Integer.toString(xmenSum));
            marvelTotalValue.setText(Integer.toString(marvelSum));
            marvelTotalValue.setText(Integer.toString(marvelSum));
            seriesTotalValue.setText(Integer.toString(seriesSum));
            TotalValue.setText(Integer.toString((marvelSum+dcSum+imageSum)));
        }
        seriesField.setText("");
        issuesField.setText("");
        dateField.setText("");
        seriesTitles.setText("");
        makeTitlesButton();
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

    public void makeMonthsButton(){
        ObservableList<MenuItem> months=month.getItems();
        MenuItem item1=new MenuItem("Overview");
        MenuItem item2=new MenuItem("January");
        MenuItem item3=new MenuItem("February");
        MenuItem item4=new MenuItem("March");
        MenuItem item5=new MenuItem("April");
        MenuItem item6=new MenuItem("May");
        MenuItem item7=new MenuItem("June");
        MenuItem item8=new MenuItem("July");
        MenuItem item9=new MenuItem("August");
        MenuItem item10=new MenuItem("September");
        MenuItem item11=new MenuItem("October");
        MenuItem item12=new MenuItem("November");
        MenuItem item13=new MenuItem("December");
        item1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                month.setText(item1.getText());
                year.setVisible(false);
                year.setText("");
                updateView();
            }
        });
        item2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                month.setText(item2.getText());
                year.setVisible(true);
                if (!year.getText().equals("")){
                    updateView();
                }
            }
        });
        item3.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                month.setText(item3.getText());
                year.setVisible(true);
                if (!year.getText().equals("")){
                    updateView();
                }
            }
        });
        item4.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                month.setText(item4.getText());
                year.setVisible(true);
                if (!year.getText().equals("")){
                    updateView();
                }
            }
        });
        item5.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                month.setText(item5.getText());
                year.setVisible(true);
                if (!year.getText().equals("")){
                    updateView();
                }
            }
        });
        item6.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                month.setText(item6.getText());
                year.setVisible(true);
                if (!year.getText().equals("")){
                    updateView();
                }
            }
        });
        item7.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                month.setText(item7.getText());
                year.setVisible(true);
                if (!year.getText().equals("")){
                    updateView();
                }
            }
        });
        item8.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                month.setText(item8.getText());
                year.setVisible(true);
                if (!year.getText().equals("")){
                    updateView();
                }
            }
        });
        item9.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                month.setText(item9.getText());
                year.setVisible(true);
                if (!year.getText().equals("")){
                    updateView();
                }
            }
        });
        item10.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                month.setText(item10.getText());
                year.setVisible(true);
                if (!year.getText().equals("")){
                    updateView();
                }
            }
        });
        item11.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                month.setText(item11.getText());
                year.setVisible(true);
                if (!year.getText().equals("")){
                    updateView();
                }
            }
        });
        item12.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                month.setText(item12.getText());
                year.setVisible(true);
                if (!year.getText().equals("")){
                    updateView();
                }
            }
        });
        item13.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                month.setText(item13.getText());
                year.setVisible(true);
                if (!year.getText().equals("")){
                    updateView();
                }
            }
        });
        months.add(item1);
        months.add(item2);
        months.add(item3);
        months.add(item4);
        months.add(item5);
        months.add(item6);
        months.add(item7);
        months.add(item8);
        months.add(item9);
        months.add(item10);
        months.add(item11);
        months.add(item12);
        months.add(item13);
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