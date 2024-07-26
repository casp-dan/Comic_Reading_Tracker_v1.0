package app.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import app.DBConnection;
import app.Entry;
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

/**
 * Controller for the make entry tab
 * @author Daniel Casper
 * @version 7/13/24
 */
public class MakeEntryDialogController {
    
    private CheckBox xmen;
    private CheckBox xmenAdj;
    private CheckBox today;
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
    @FXML private Label darkHorseTotalValue;
    @FXML private Label boomTotalValue;
    @FXML private Label xmenTotalValue;
    @FXML private Label seriesTotal;
    @FXML private Label seriesTotalValue;
    
    
    private ArrayList<String> MONTHS=new ArrayList<String>(Arrays.asList("Overview","Yearly","January","February","March","April","May","June","July","August","September","October","November","December"));
    private ArrayList<String> YEARS=new ArrayList<String>(Arrays.asList("2022","2023","2024"));
    private ArrayList<String> PUBLISHERS=new ArrayList<String>(Arrays.asList("DC","Marvel","Image","Dark Horse", "Boom"));


    /**
     * Sets objects and creates Menu Buttons for the publisher, series title, month and year.
     */
    public void setObjects() {
        xmen=new CheckBox("X-Men?");
        xmenAdj=new CheckBox("X-Men Adjacent?");
        today=new CheckBox("Today?");
        seriesField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent t) {
                makeTitlesButton();
            }
        });
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
        xmen.setLayoutX(100);
        xmen.setVisible(false);
        xmenAdj.setLayoutX(100);
        xmenAdj.setLayoutY(30);
        xmenAdj.setVisible(false);
        year=new MenuButton("");
        year.setLayoutX(350);
        year.setVisible(false);
        today.setLayoutY(245);
        today.setLayoutX(185);
        updateView();
        pane.getChildren().add(1, xmen);
        pane.getChildren().add(1, xmenAdj);
        pane.getChildren().add(1, year);
        pane.getChildren().add(1, today);
        makeYearButton();
        makeMonthsButton();
        makePublisherButton();
        makeTitlesButton();
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
            if (properDate(dateField.getText())){
                Entry entry=new Entry(seriesField.getText(), issuesField.getText(), dateField.getText(), publisher.getText(), xmen.isSelected(),xmenAdj.isSelected());
                entry.makeEntry();
                updateView();
            }
        }
    }

    /**
     * Creates a dropdown menu button to select the publisher of a new comic series.
     */
    public void makePublisherButton(){
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
     * Updates stat values based on month and year button values.
     */
    public void updateView(){
        int monthInt=MONTHS.indexOf(month.getText())-1;
        if (month.getText().equals("") || month.getText().equals("Overview")){
            setStatValues(DBConnection.getTotal(), DBConnection.getNumXMen(), DBConnection.getNumPublisher("DC"), DBConnection.getNumPublisher("Marvel"), DBConnection.getNumPublisher("Image"), DBConnection.getNumPublisher("Dark Horse"), DBConnection.getNumPublisher("Boom"), DBConnection.getNumSeries());
            year.setVisible(false);
            year.setText("");
        }
        else if (month.getText().equals("Yearly")){
            createYearlyView();
        }
        else{
            createMonthlyView(monthInt);
        }
        publisher.setText("");
        xmen.setVisible(false);
        xmen.setSelected(false);
        xmenAdj.setVisible(false);
        xmenAdj.setSelected(false);
        seriesField.clear();
        issuesField.clear();
        dateField.clear();
        seriesTitles.setText("");
        today.setSelected(false);;
        makeTitlesButton();
    }

    /**
     * Sets the text for each label in the stats section of the tab.
     * @param total Total number of individual rows in Comic table
     * @param xmen Total number of individual rows in comic table that is part of an X-Men or X-Men Adjacent Series
     * @param dc Total number of individual rows in comic table that is part of a DC Series
     * @param marvel Total number of individual rows in comic table that is part of a Marvel Series
     * @param image Total number of individual rows in comic table that is part of an Image Series
     * @param darkHorse Total number of individual rows in comic table that is part of a Dark Horse Series
     * @param boom Total number of individual rows in comic table that is part of a Boom Series
     * @param series Total number of individual rows in Series table
     */
    private void setStatValues(int total, int xmen, int dc, int marvel, int image, int darkHorse, int boom, int series){
        TotalValue.setText(Integer.toString(total));
        dcTotalValue.setText(Integer.toString(dc));                   
        imageTotalValue.setText(Integer.toString(image)); 
        darkHorseTotalValue.setText(Integer.toString(darkHorse)); 
        boomTotalValue.setText(Integer.toString(boom)); 
        xmenTotalValue.setText(Integer.toString(xmen));
        marvelTotalValue.setText(Integer.toString(marvel));
        seriesTotalValue.setText(Integer.toString(series));
    }

    /**
     * Creates a dropdown menu button to select the title of a series that already exists.
     */
    public void makeTitlesButton(){
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
     * Creates a dropdown menu button to select the month for the stats view. Includes total overview and yearly view options.
     */
    public void makeMonthsButton(){
        ObservableList<MenuItem> monthsItems=month.getItems();
        for (int i=0;i<MONTHS.size();i++){
            MenuItem item=new MenuItem(MONTHS.get(i));
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

    /**
     * Creates a dropdown menu button to select the year for the stats view.
     */
    public void makeYearButton(){
        ObservableList<MenuItem> years=year.getItems();
        for (int i=0;i<YEARS.size();i++){
            MenuItem item=new MenuItem(YEARS.get(i));
            item.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    year.setText(item.getText());
                    updateView();
                }
            });
            years.add(item);
        }
    }

    /**
     * Verifies that the contents of the date Text Field contains a valid date in the proper form of mm/dd/yyyy.
     * Years are stored only by the last 2 digits (ie: 2024-->24) and years entered as 2 digits will be accepted as well. 
     * @param dateString Text string retrieved from the date text field
     * @return true if date is properly formatted, false if not 
     */
    private boolean properDate(String dateString){
        if (dateString.split("/").length!=3){
            errorMessage("Fields Empty", "Please Properly Enter Date!");
            return false;
        }
        return true;
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
     * Retrieves integer values for stats based on the month and year 
     * button selections if a calendar month is selected
     * @param monthInt integer value for the month field (1-12)
     */
    private void createMonthlyView(int monthInt){
        int dcSum=0;
        int xmenSum=0;
        int marvelSum=0;
        int imageSum=0;
        int darkHorseSum=0;
        int boomSum=0;
        int seriesSum=0;
        String yearStr=year.getText().split("0")[1];
        TotalValue.setText(Integer.toString(DBConnection.getTotalMonth(monthInt,Integer.parseInt(yearStr))));
        int idMax=DBConnection.getFinalSeriesID();
        for (int i=1;i<=idMax;i++){
            String publisher=DBConnection.getPublisherByID(i);
            int addTo=DBConnection.getNumByMonth(i,monthInt,Integer.parseInt(yearStr));
            switch(publisher){
                case "Marvel": marvelSum+=addTo; break; 
                case "Image": imageSum+=addTo; break;
                case "Dark Horse": darkHorseSum+=addTo; break;
                case "Boom": boomSum+=addTo; break;
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
        setStatValues((marvelSum+dcSum+imageSum+darkHorseSum+boomSum), xmenSum, dcSum, marvelSum, imageSum, darkHorseSum, boomSum, seriesSum);
    }

    /**
     * Retrieves integer values for stats based on the year 
     * button selection if "yearly" is selected on the month button
     */
    private void createYearlyView(){
        int dcSum=0;
        int xmenSum=0;
        int marvelSum=0;
        int imageSum=0;
        int darkHorseSum=0;
        int boomSum=0;
        int seriesSum=0;
        String yearStr=year.getText().split("0")[1];
        TotalValue.setText(Integer.toString(DBConnection.getTotalYear(Integer.parseInt(yearStr))));
        int idMax=DBConnection.getFinalSeriesID();
        for (int i=1;i<=idMax;i++){
            String publisher=DBConnection.getPublisherByID(i);
            int addTo=DBConnection.getNumByYear(i,Integer.parseInt(yearStr));
            switch(publisher){
                case "Marvel": marvelSum+=addTo; break; 
                case "Image": imageSum+=addTo; break;
                case "Dark Horse": darkHorseSum+=addTo; break;
                case "Boom": boomSum+=addTo; break;
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
        setStatValues((marvelSum+dcSum+imageSum+darkHorseSum+boomSum), xmenSum, dcSum, marvelSum, imageSum, darkHorseSum, boomSum, seriesSum);
    }

    /**
     * Gets and formats the current date in the form of mm/dd/yy
     * @return a string of the current date in the form mm/dd/yy
     */
    private String getToday(){
        java.time.LocalDate ldt =java.time.LocalDate.now();
        String[] today=ldt.toString().split("-");
        if (Integer.parseInt(today[1])<10){
            today[1]=today[1].split("0")[1];
        }
        today[0]=today[0].split("0")[1];
        return today[1]+"/"+today[2]+"/"+today[0];
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