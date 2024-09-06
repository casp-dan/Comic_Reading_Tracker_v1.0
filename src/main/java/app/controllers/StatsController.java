package app.controllers;

import java.util.ArrayList;
import java.util.Arrays;

import app.DBConnection;
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

    private ArrayList<String> MONTHS=new ArrayList<String>(Arrays.asList("Overview","Yearly","January","February","March","April","May","June","July","August","September","October","November","December"));
    private ArrayList<String> YEARS=new ArrayList<String>(Arrays.asList("2022","2023","2024"));

    /**
     * Sets objects and creates Menu Buttons for the publisher, series title, month and year.
     */
    public void setObjects() {
        year=new MenuButton("");
        year.setLayoutX(150);
        year.setVisible(false);
        updateStats();
        makePercentBox();
        pane.getChildren().add(1, year);
        makeYearButton();
        makeMonthsButton();
        updateStats();
    }

    /**
     * Updates stat values based on month and year button values.
     */
    public void updateStats(){
        int monthInt=MONTHS.indexOf(month.getText())-1;
        if (month.getText().equals("") || month.getText().equals("Overview")){
            setStatValues(DBConnection.getTotal(), DBConnection.getNumXMen(), DBConnection.getNumPublisher("DC"), DBConnection.getNumPublisher("Marvel"), DBConnection.getNumPublisher("Image"), DBConnection.getNumPublisher("Dark Horse"), DBConnection.getNumPublisher("Boom"), DBConnection.getNumSeries());
            year.setVisible(false);
            year.setText("");
        }
        else if (month.getText().equals("Yearly")){
            createView(0);
        }
        else{
            createView(monthInt);
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
            if (month.getText().equals("") || month.getText().equals("Overview")){
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
        for (int i=0;i<MONTHS.size();i++){
            MenuItem item=new MenuItem(MONTHS.get(i));
            item.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    month.setText(item.getText());
                    year.setVisible(true);
                    if (!year.getText().equals("")){
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
        ObservableList<MenuItem> years=year.getItems();
        for (int i=0;i<YEARS.size();i++){
            MenuItem item=new MenuItem(YEARS.get(i));
            item.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    year.setText(item.getText());
                    updateStats();
                }
            });
            years.add(item);
        }
    }

    /**
     * Retrieves integer values for stats based on the month and year 
     * button selections if a calendar month is selected
     * @param monthInt integer value for the month field (1-12)
     */
    private void createView(int monthInt){
        int dcSum=0;
        int xmenSum=0;
        int marvelSum=0;
        int imageSum=0;
        int darkHorseSum=0;
        int boomSum=0;
        int seriesSum=0;
        String yearStr=year.getText().split("0")[1];
        if (monthInt==0){
            TotalValue.setText(Integer.toString(DBConnection.getTotalYear(Integer.parseInt(yearStr))));
        }
        else{
            TotalValue.setText(Integer.toString(DBConnection.getTotalMonth(monthInt,Integer.parseInt(yearStr))));
        }
        int idMax=DBConnection.getFinalSeriesID();
        for (int i=1;i<=idMax;i++){
            String publisher=DBConnection.getPublisherByID(i);
            int addTo;
            if (monthInt==0){
                addTo=DBConnection.getNumByYear(i,Integer.parseInt(yearStr));
            }
            else{
                addTo=DBConnection.getNumByMonth(i,monthInt,Integer.parseInt(yearStr));
            }
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
            if (monthInt==0){
                xmenSum+=DBConnection.getNumByYear(list.get(x),Integer.parseInt(yearStr));
            }
            else{
                xmenSum+=DBConnection.getNumByMonth(list.get(x),monthInt,Integer.parseInt(yearStr));
            }
            x++;
        }
        list=DBConnection.getXmenAdj();
        x=0;
        while (x<list.size()){
            if (monthInt==0){
                xmenSum+=DBConnection.getNumXmenByYear(list.get(x),Integer.parseInt(yearStr));
            }
            else{
                xmenSum+=DBConnection.getNumXmenByMonth(list.get(x),monthInt,Integer.parseInt(yearStr));
            }
            x++;
        }
        setStatValues((marvelSum+dcSum+imageSum+darkHorseSum+boomSum), xmenSum, dcSum, marvelSum, imageSum, darkHorseSum, boomSum, seriesSum);
    }
}