package app.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import app.DBConnection;
import app.Entry;
import app.MainScenesController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import models.Date;
import java.time.LocalDate;

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
    
    private ArrayList<String> PUBLISHERS=new ArrayList<String>(Arrays.asList("DC","Marvel","Image","Dark Horse", "Boom"));


    /**
     * Sets objects and creates Menu Buttons for the publisher, series title, month and year.
     */
    public void setObjects(MainScenesController main) {
        mainController=main;
        makeXmenButton();
        makeTitlesButton();
        makeSeriesSearch();
        makeXmenAdjButton();
        makeTodayCheckbox();
        makePublisherButton();
    }

    /**
     * Makes a new entry based on the filled out text fields, or creates an error message if needed
     * @param mouseEvent
     * @throws IOException 
     */
    public void makeEntry(@SuppressWarnings("exports") MouseEvent mouseEvent) throws IOException {
        if (seriesField.getText().equals("") || issuesField.getText().equals("") || dateField.getText().equals("")){
            errorMessage("Fields Empty", "Please Fill Out All Fields!");
        }
        else if (issuesField.getText().contains(",")){
            String[] issues=issuesField.getText().split(",");
            String[] dates=dateField.getText().split(",");
            if (issues.length!=dates.length){
                errorMessage("Improper Entry", "Please ensure each set of issues has a correspondig date!");
            }
            else{
                String publisherStr=publisher.getText();
                String seriesName=seriesField.getText();
                boolean isXmen=xmen.isSelected();
                boolean isXmenAdj=xmenAdj.isSelected();
                for (int i=0;i<issues.length;i++){
                    Date date=new Date(dates[i]);
                    if (date.toString().equals("0/0/0")){
                        Entry entry=new Entry(seriesName, issues[i], date, publisherStr, isXmen,isXmenAdj);
                        if (!entry.makeEntry()){
                            i=issues.length;
                        }
                        clearFields();           
                    }     
                }
            }
        }
        else{
            Date date=new Date(dateField.getText());
            if (date.toString().equals("0/0/0")){
                Entry entry=new Entry(seriesField.getText(), issuesField.getText(), date, publisher.getText(), xmen.isSelected(),xmenAdj.isSelected());
                entry.makeEntry();
                clearFields();
            }
        }
    }
    
    /**
     * Clear all text fields and buttons and update 
     * the series button's contents
     */
    private void clearFields(){
        dateField.clear();
        makeTitlesButton();
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
     * Creates a dropdown menu button to select the title of a series that already exists.
     */
    private void makeTitlesButton(){
        ObservableList<MenuItem> bookNames=seriesTitles.getItems();
        bookNames.clear();
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
                makeTitlesButton();
            }
        });
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
}