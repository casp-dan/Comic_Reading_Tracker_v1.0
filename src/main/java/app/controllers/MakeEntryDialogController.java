package app.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import app.DBConnection;
import app.MainScenesController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import models.Date;
import models.Entry;

/**
 * Controller for the make entry tab
 * @author Daniel Casper
 * @version 7/13/24
 */
public class MakeEntryDialogController {
    
    private CheckBox xmen;
    private CheckBox xmenAdj;
    private CheckBox today;
    private MainScenesController mainController;
    @FXML private AnchorPane pane;
    @FXML private MenuButton publisher;
    @FXML private MenuButton seriesTitles;
    @SuppressWarnings("exports")
    @FXML public TextField seriesField;
    @SuppressWarnings("exports")
    @FXML public TextField dateField;
    @SuppressWarnings("exports")
    @FXML public TextField issuesField; 
    
    private final ArrayList<String> PUBLISHERS=new ArrayList<String>(Arrays.asList("DC","Marvel","Image","Dark Horse", "Boom", "Other"));


    /**
     * Sets objects and creates Menu Buttons for the publisher, series title, month and year.
     */
    public void setObjects(MainScenesController main) {
        mainController=main;
        makeXmenButton();
        makeSeriesSearch();
        makeXmenAdjButton();
        makeTodayCheckbox();
        makePublisherButton();
        mainController.makeTitlesButton(seriesTitles,seriesField);
        actionOnEnter();
    }

    /**
     * Makes a new entry based on the filled out text fields, or creates an error message if needed
     * @param mouseEvent
     * @throws IOException 
     */
    public void entryButton(@SuppressWarnings("exports") MouseEvent mouseEvent){
        if (seriesField.getText().equals("") || issuesField.getText().equals("") || dateField.getText().equals("")){
            mainController.errorMessage("Fields Empty", "Please Fill Out All Fields!");
        }
        else if (issuesField.getText().contains(",")){
            String[] issues=issuesField.getText().split(",");
            String[] dates=dateField.getText().split(",");
            if (issues.length!=dates.length){
                mainController.errorMessage("Improper Entry","Please ensure each set of issues has a correspondig date!");
            }
            else{
                String publisherStr=publisher.getText();
                String seriesName=seriesField.getText();
                boolean isXmen=xmen.isSelected();
                boolean isXmenAdj=xmenAdj.isSelected();
                for (int i=0;i<issues.length;i++){
                    Date date=new Date(dates[i]);
                    if (!date.toString().equals("0/0/0")){
                        Entry entry=new Entry(seriesName,issues[i],date,publisherStr,isXmen,isXmenAdj);
                        if (!makeEntry(entry)){
                            i=issues.length;
                        }
                    }     
                }
            }
        }
        else{
            Date date=new Date(dateField.getText());
            if (!date.toString().equals("0/0/0")){
                Entry entry=new Entry(seriesField.getText(),issuesField.getText(),date,publisher.getText(),xmen.isSelected(),xmenAdj.isSelected());
                makeEntry(entry);
            }
        }
    }
    
    /**
     * Clear all text fields and buttons and update 
     * the series button's contents
     */
    private void clearFields(){
        dateField.clear();
        mainController.makeTitlesButton(seriesTitles,seriesField);
        seriesField.clear();
        issuesField.clear();
        publisher.setText("");
        mainController.updateTabs();
        xmen.setVisible(false);
        xmen.setSelected(false);
        seriesTitles.setText("");
        today.setSelected(false);
        xmenAdj.setVisible(false);
        xmenAdj.setSelected(false);
    }
    
    /**
     * Creates a dropdown menu button to select the publisher of a new comic series.
     */
    private void makePublisherButton(){
        ObservableList<MenuItem> publishers=publisher.getItems();
        for (int i=0;i<PUBLISHERS.size();i++){
            MenuItem item=new MenuItem(PUBLISHERS.get(i));
            if (PUBLISHERS.get(i).equals("Marvel")){
                item.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        publisher.setText(item.getText());
                        xmen.setVisible(true);
                        xmenAdj.setVisible(true);
                    }
                });
                publishers.add(item);
            }
            else{
                item.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        publisher.setText(item.getText());
                        xmen.setVisible(false);
                        xmen.setSelected(false);
                        xmenAdj.setVisible(false);
                        xmenAdj.setSelected(false);
                    }
                });
                publishers.add(item);
            }
        }
    }

    /**
     * Creates a checkbox element to indicate whether 
     * a marvel series is specifically an x-men book.
     */
    private void makeXmenButton(){
        xmen=new CheckBox("X-Men?");
        xmen.setLayoutX(100);
        xmen.setVisible(false);
        pane.getChildren().add(1, xmen);
    }
    
    /**
     * Creates a checkbox element to indicate whether 
     * a specific issue of a non x-men marvel series 
     * is specifically an x-men adjacent issue.
     */
    private void makeXmenAdjButton(){
        xmenAdj=new CheckBox("X-Men Adjacent?");
        xmenAdj.setLayoutX(100);
        xmenAdj.setLayoutY(30);
        xmenAdj.setVisible(false);
        pane.getChildren().add(1, xmenAdj);
    }

    /**
     * Creates a checkbox element that automatically 
     * fills the date text field with the current date 
     * based on the LocalDate method for all issues in
     * the issue field.
     */
    private void makeTodayCheckbox(){
        today=new CheckBox("Today?");
        today.setLayoutY(245);
        today.setLayoutX(185);
        today.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent t) {
                if (today.isSelected()){
                    setDate();
                }
                else{
                    dateField.clear();
                }
            }
        });
        pane.getChildren().add(1, today);
    }

    /**
     * Adds an event handler to the series text 
     * field to search the Series table by substring.
     */
    private void makeSeriesSearch(){
        seriesField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent t) {
                mainController.makeTitlesButton(seriesTitles,seriesField);
            }
        });
    }
    
    /**
     * Gets and formats the current date in the form of mm/dd/yy
     * @return a string of the current date in the form mm/dd/yy
     */
    private String getToday(){
        Date today=new Date(LocalDate.now());
        return today.toString();
    }

    /**
     * Sets the date text field as the current date. 
     * Will generate duplicate dates as necessary.
     */
    private void setDate(){
        String issues=issuesField.getText();
        if (issues.contains(",")){
            String date="";
            String[] issueList=issues.split(",");
            int x=0;
            while (x<issueList.length){
                date+=getToday()+",";
                x++;
            }
            dateField.setText(date);
        }
        else{
            dateField.setText(getToday());
        }
    }

    /**
     * Creates a new entry in the database, creating a new series if needed and adding all indicated issues
     * @return true if entry made successfully, false if error message is displayed
     */
    private boolean makeEntry(Entry entry){
        // int bookID=DBConnection.getSeriesIDByTitle(entry.getSeriesName());
        // if (bookID!=0){
        //     addBook(entry,bookID);
        //     clearFields();
        //     return true;
        // }
        if (entry.getPublisher().equals("")){
            mainController.errorMessage("No Publisher Selected", "Please Select a Publisher");
            return false;
        }
        else{
            // order,  SeriesName,  Publisher,  xmen
            DBConnection.createSeries(DBConnection.getTotalSeries(), entry.getSeriesName(), entry.getPublisher(), entry.getXmen()); 
            addBook(entry,entry.getSeriesName());
            clearFields();
            return true;
        }
        // }
    }

    /**
     * Adds either a set of several issues or a single issue as new rows in the Comic table
     * @param seriesName integer ID for a series (correlates to SeriesID in all tables in database)
     */
    private void addBook(Entry entry, String seriesName){
        Date date=entry.getDate();
        for (String issueName: entry.getIssues()){
            int orderNum=DBConnection.getTotalIssues()+1;
            DBConnection.addIssue(orderNum,issueName,seriesName,entry.getXmenAdj(),date.toDateString());
            // if (!DBConnection.entryExists(bookID,issue,date.toString(),date.getMonth(),date.getDay(),date.getYear())){
            // }
            // else{
                // mainController.errorMessage("Entry Exists", "This Entry Exists");
            // }
        }
    }

    private void actionOnEnter(){
        xmen.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    entryButton(null);
                }
            }
        });
        xmenAdj.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    entryButton(null);
                }
            }
        });
        publisher.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    entryButton(null);
                }
            }
        });
        seriesTitles.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    entryButton(null);
                }
            }
        });
        issuesField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    entryButton(null);
                }
            }
        });
        seriesField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    entryButton(null);
                }
            }
        });
        dateField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    entryButton(null);
                }
            }
        });
        today.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    entryButton(null);
                }
            }
        });
    }
}