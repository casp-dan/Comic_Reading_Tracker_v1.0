package app.controllers;

import java.util.ArrayList;
import java.util.Arrays;

import app.DBConnection;
import app.MainScenesController;
import models.Date;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.CheckBox;
import java.time.LocalDate;



/**
 * Controller for the stats page.
 * @author Daniel Casper
 */

public class StatsController {

    private MenuButton year;
    private MainScenesController mainController;
    @FXML private AnchorPane pane;
    @FXML private MenuButton month;
    @FXML private Label TotalValue;
    @FXML private Label marvelTotalValue;
    @FXML private Label dcTotalValue;
    @FXML private Label imageTotalValue;
    @FXML private Label darkHorseTotalValue;
    @FXML private Label boomTotalValue;
    @FXML private Label xmenTotalValue;
    @FXML private Label seriesTotalValue;
    @FXML private CheckBox percents;
    private CheckBox upTo;

    private final ArrayList<String> MONTHS=new ArrayList<String>(Arrays.asList("Overview","Yearly","January","February","March","April","May","June","July","August","September","October","November","December"));
    private final ArrayList<String> YEARS=new ArrayList<String>(Arrays.asList("2022","2023","2024"));

    /**
     * Sets objects and creates Menu Buttons for the publisher, series title, month and year.
     */
    public void setObjects(MainScenesController main) {
        mainController=main;
        makeYearButton();
        makeUpToBox();
        updateStats();
        makePercentBox();
        makeMonthsButton();
        updateStats();
    }

    /**
     * Updates stat values based on month and year button values.
     */
    public void updateStats(){
        int monthInt=MONTHS.indexOf(month.getText())-1;
        if (month.getText().isEmpty() || month.getText().equals("Overview")){
            setStatValues(DBConnection.getTotal(), DBConnection.getNumXMen(), DBConnection.getNumPublisher("DC"), DBConnection.getNumPublisher("Marvel"), DBConnection.getNumPublisher("Image"), DBConnection.getNumPublisher("Dark Horse"), DBConnection.getNumPublisher("Boom"), DBConnection.getNumSeries());
            year.setVisible(false);
            year.setText("");
        }
        else if (month.getText().equals("Yearly")){
            ArrayList<Integer> totals=DBConnection.tempTableYear(Integer.parseInt(year.getText().split("0")[1]));
            setStatValues(totals.get(0),totals.get(1),totals.get(2),totals.get(3),totals.get(4),totals.get(5),totals.get(6),totals.get(7));
        }
        else{
            if (upTo.isSelected()){
                ArrayList<Integer> totals = DBConnection.tempTableSnap(Integer.parseInt(year.getText().split("0")[1]), monthInt);
                setStatValues(totals.get(0), totals.get(1), totals.get(2), totals.get(3), totals.get(4), totals.get(5), totals.get(6), totals.get(7));
            }
            else {
                ArrayList<Integer> totals = DBConnection.tempTableMonth(Integer.parseInt(year.getText().split("0")[1]), monthInt);
                setStatValues(totals.get(0), totals.get(1), totals.get(2), totals.get(3), totals.get(4), totals.get(5), totals.get(6), totals.get(7));
            }
        }
    }

    private void makePercentBox(){
        percents.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent t) {
                if (percents.isSelected()){
                    setStatValues(Integer.parseInt(TotalValue.getText()), Integer.parseInt(xmenTotalValue.getText()), Integer.parseInt(dcTotalValue.getText()), Integer.parseInt(marvelTotalValue.getText()), Integer.parseInt(imageTotalValue.getText()), Integer.parseInt(darkHorseTotalValue.getText()), Integer.parseInt(boomTotalValue.getText()), Integer.parseInt(seriesTotalValue.getText()));
                }
                else{
                    updateStats();
                }
            }
        });
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
        if (percents.isSelected()){
            Date today=new Date(LocalDate.now());
            int numMonths=today.getNumMonths();
            TotalValue.setText(Integer.toString(total));
            dcTotalValue.setText((String.format("%.2f", (((double)dc/(double)total)*100)))+"%");                   
            imageTotalValue.setText((String.format("%.2f", (((double)image/(double)total)*100)))+"%"); 
            darkHorseTotalValue.setText((String.format("%.2f", (((double)darkHorse/(double)total)*100)))+"%"); 
            boomTotalValue.setText((String.format("%.2f", (((double)boom/(double)total)*100)))+"%"); 
            xmenTotalValue.setText((String.format("%.2f", (((double)xmen/(double)marvel)*100)))+"%");
            marvelTotalValue.setText((String.format("%.2f", (((double)marvel/(double)total)*100)))+"%");
            if (month.getText().isEmpty() || month.getText().equals("Overview")){
                seriesTotalValue.setText(String.format("%.2f",((double)series/(double)numMonths)));
            }
            else{
                seriesTotalValue.setText(Integer.toString(series));
            }
        }
        else{
            TotalValue.setText(Integer.toString(total));
            dcTotalValue.setText(Integer.toString(dc));                   
            imageTotalValue.setText(Integer.toString(image)); 
            darkHorseTotalValue.setText(Integer.toString(darkHorse)); 
            boomTotalValue.setText(Integer.toString(boom)); 
            xmenTotalValue.setText(Integer.toString(xmen));
            marvelTotalValue.setText(Integer.toString(marvel));
            seriesTotalValue.setText(Integer.toString(series));
        }
    }

    /**
     * Creates a dropdown menu button to select the month for the stats view. Includes total overview and yearly view options.
     */
    private void makeMonthsButton(){
        ObservableList<MenuItem> monthsItems=month.getItems();
        for (String s : MONTHS) {
            MenuItem item = new MenuItem(s);
            item.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    month.setText(item.getText());
                    year.setVisible(true);
                    if (!year.getText().isEmpty()) {
                        updateStats();
                    }
                }
            });
            monthsItems.add(item);
        }
    }

    /**
     * Creates a dropdown menu button to select the year for the stats view.
     */
    private void makeYearButton(){
        year=new MenuButton("");
        year.setLayoutX(150);
        year.setVisible(false);
        ObservableList<MenuItem> years=year.getItems();
        for (String yearStr : YEARS) {
            MenuItem item = new MenuItem(yearStr);
            item.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    year.setText(item.getText());
                    updateStats();
                }
            });
            years.add(item);
        }
        pane.getChildren().add(1, year);
    }
    
    /**
     * Creates a dropdown menu button to select the year for the stats view.
     */
    private void makeUpToBox(){
        upTo=new CheckBox("Switch Modes");
        upTo.setLayoutX(215);
        upTo.setLayoutY(5);
        upTo.setVisible(true);
        upTo.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent t) {
                if (year.getText().isEmpty() && upTo.isSelected()){
                    mainController.errorMessage("Fields Empty", "Select a Month to Snapshot!");
                    upTo.setSelected(false);
                }
                else{
                    updateStats();
                }
            }
        });
        pane.getChildren().add(1, upTo);
    }
}
