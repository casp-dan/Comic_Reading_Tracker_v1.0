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
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.CheckBox;
import java.time.LocalDate;
import java.time.LocalDateTime;



/**
 * Controller for the stats page.
 * @author Daniel Casper
 */

public class StatsController {

    private MenuButton year;
    private MainScenesController mainController;
    @FXML private VBox box;
    @FXML private AnchorPane pane;
    @FXML private MenuButton month;
    @FXML private CheckBox percents;
    private CheckBox upTo;
    private ArrayList<Label> values;

    private final ArrayList<String> MONTHS=new ArrayList<String>(Arrays.asList("Overview","Yearly","January","February","March","April","May","June","July","August","September","October","November","December"));
    private final ArrayList<String> YEARS=new ArrayList<String>(Arrays.asList("2022","2023","2024","2025"));

    /**
     * Sets objects and creates Menu Buttons for the publisher, series title, month and year.
     */
    public void setObjects(MainScenesController main) {
        mainController=main;
        makePage();
        makeYearButton();
        makeUpToBox();
        updateStats();
        makePercentBox();
        makeMonthsButton();
        updateStats();
    }

    public void makePage(){
        values=new ArrayList<Label>();
        box.getChildren().clear();
        Label TotalLabel=new Label("Total:");
        TotalLabel.setPrefWidth(200.0);
        TotalLabel.setPrefHeight(16.0);
        TotalLabel.setTextAlignment(TextAlignment.CENTER);
        TotalLabel.setTextFill(Color.WHITE);
        
        Label TotalValue=new Label();
        TotalValue=new Label();
        TotalValue.setPrefWidth(200.0);
        TotalValue.setPrefHeight(16.0);
        TotalValue.setTextAlignment(TextAlignment.CENTER);
        TotalValue.setTextFill(Color.WHITE);

        box.getChildren().add(TotalLabel);
        box.getChildren().add(TotalValue);
        values.add(TotalValue);

        ArrayList<String> pub_list=DBConnection.getPublishers();
        for (int i=0;i<pub_list.size();i++){
            Separator sep=new Separator();
            sep.setOrientation(Orientation.VERTICAL);
            sep.setPrefHeight(0);
            sep.setPrefWidth(6.0);
            sep.setVisible(false);

            Label label=new Label("Total "+pub_list.get(i)+":");
            label.setPrefWidth(200.0);
            label.setPrefHeight(16.0);
            label.setTextAlignment(TextAlignment.CENTER);
            label.setTextFill(Color.WHITE);

            Label value=new Label();
            value.setPrefWidth(200.0);
            value.setPrefHeight(16.0);
            value.setTextAlignment(TextAlignment.CENTER);
            value.setTextFill(Color.WHITE);

            box.getChildren().addAll(sep);
            box.getChildren().add(label);
            box.getChildren().add(value);
            values.add(value);
        }

        Separator sep=new Separator();
        sep.setOrientation(Orientation.VERTICAL);
        sep.setPrefHeight(0);
        sep.setPrefWidth(6.0);
        sep.setVisible(false);

        Label xmenLabel=new Label("Total X-Men:");
        xmenLabel.setPrefWidth(200.0);
        xmenLabel.setPrefHeight(16.0);
        xmenLabel.setTextAlignment(TextAlignment.CENTER);
        xmenLabel.setTextFill(Color.WHITE);

        Label xmenTotalValue=new Label("");
        xmenTotalValue.setPrefWidth(200.0);
        xmenTotalValue.setPrefHeight(16.0);
        xmenTotalValue.setTextAlignment(TextAlignment.CENTER);
        xmenTotalValue.setTextFill(Color.WHITE);
        
        
        box.getChildren().add(sep);
        box.getChildren().add(xmenLabel);
        box.getChildren().add(xmenTotalValue);
        values.add(xmenTotalValue);

        sep=new Separator();
        sep.setOrientation(Orientation.VERTICAL);
        sep.setPrefHeight(0);
        sep.setPrefWidth(6.0);
        sep.setVisible(false);
        
        Label seriesLabel=new Label("Total Series:");
        seriesLabel.setPrefWidth(200.0);
        seriesLabel.setPrefHeight(16.0);
        seriesLabel.setTextAlignment(TextAlignment.CENTER);
        seriesLabel.setTextFill(Color.WHITE);
        
        Label seriesTotalValue=new Label("");
        seriesTotalValue=new Label("Tester");
        seriesTotalValue.setPrefWidth(200.0);
        seriesTotalValue.setPrefHeight(16.0);
        seriesTotalValue.setTextAlignment(TextAlignment.CENTER);
        seriesTotalValue.setTextFill(Color.WHITE);

        box.getChildren().add(sep);
        box.getChildren().add(seriesLabel);
        box.getChildren().add(seriesTotalValue);
        values.add(seriesTotalValue);

    }

    /**
     * Updates stat values based on month and year button values.
     */
    public void updateStats(){
        int monthInt=MONTHS.indexOf(month.getText())-1;
        if (month.getText().isEmpty()){
            ArrayList<Integer> totals=new ArrayList<Integer>();
            ArrayList<String> pub_list=DBConnection.getPublishers();
            totals.add(DBConnection.getTotalIssues());
            for (int i=0;i<pub_list.size();i++){
                totals.add(DBConnection.getNumPublisher(pub_list.get(i)));

            }
            totals.add(DBConnection.getNumXMen());
            totals.add(DBConnection.getTotalSeries());
            setStatValues(totals);
            year.setVisible(false);
            year.setText("");
        }
        else if (month.getText().equals("Yearly")){
            ArrayList<Integer> totals=DBConnection.tempTableYear(year.getText().split("0")[1]);
            setStatValues(totals);
        }
        else{
            if (upTo.isSelected()){
                ArrayList<Integer> totals = DBConnection.tempTableSnap(year.getText().split("0")[1], Integer.toString(monthInt));
                setStatValues(totals);
            }
            else {
                String sMonth=Integer.toString(monthInt);
                if (monthInt<10){
                    sMonth="0"+sMonth;
                }
                ArrayList<Integer> totals = DBConnection.tempTableMonth(year.getText(), sMonth);
                setStatValues(totals);
            }
        }
    }

    private ArrayList<Integer> setPercents(ArrayList<Integer> totals){
        if (percents.isSelected()){
            double marvel=(double) (totals.get(2));
            for (int i=1;i<values.size()-2;i++){
                int z=(int) Math.round(((double) totals.get(i)/(double) totals.get(0))*100);
                totals.set(i,(int) Math.round(((double) totals.get(i)/(double) totals.get(0))*100));
                totals.set(i,z);
            }
            totals.set(totals.size()-2,(int) Math.round(((double) (totals.get(totals.size()-2))/marvel)*100));
        }
        return totals;
    }

    private void makePercentBox(){
        percents.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent t) {
                updateStats();
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
     * @param other Total number of individual rows in comic table that is part of a series published by any other independent publishing house
     * @param series Total number of individual rows in Series table
     */
    private void setStatValues(ArrayList<Integer> totals){//int total, int xmen, int dc, int marvel, int image, int darkHorse, int boom, int other, int series){
        if (percents.isSelected()){
            Date today=new Date(LocalDateTime.now());
            int numMonths=today.getNumMonths();
            //TotalValue.setText(Integer.toString(totals.get(0)));
            for (int i=0;i<values.size();i++){
                values.get(i).setText(Integer.toString(totals.get(i)));
            }
            if (month.getText().isEmpty() || month.getText().equals("Overview")){
                values.get(values.size()-2).setText(String.format("%.2f",((double)totals.get(totals.size()-1)/(double)numMonths)));
            }
            else{
                values.get(values.size()-1).setText(Integer.toString(totals.get(totals.size()-1)));
            }
            if (percents.isSelected()){
                totals=setPercents(totals);
                for (int i=1;i<totals.size()-1;i++){
                    values.get(i).setText(Integer.toString(totals.get(i))+"%");
                }
            }

            // dcTotalValue.setText((String.format("%.2f", (((double)dc/(double)total)*100)))+"%");                   
            // imageTotalValue.setText((String.format("%.2f", (((double)image/(double)total)*100)))+"%"); 
            // darkHorseTotalValue.setText((String.format("%.2f", (((double)darkHorse/(double)total)*100)))+"%"); 
            // boomTotalValue.setText((String.format("%.2f", (((double)boom/(double)total)*100)))+"%");
            // otherTotalValue.setText((String.format("%.2f", (((double)other/(double)total)*100)))+"%");
            // xmenTotalValue.setText((String.format("%.2f", (((double)xmen/(double)marvel)*100)))+"%");
            // marvelTotalValue.setText((String.format("%.2f", (((double)marvel/(double)total)*100)))+"%");
            // if (month.getText().isEmpty() || month.getText().equals("Overview")){
            //     seriesTotalValue.setText(String.format("%.2f",((double)series/(double)numMonths)));
            // }
            // else{
            //     seriesTotalValue.setText(Integer.toString(series));
            // }
        }
        else{
            for (int i=0;i<values.size();i++){
                values.get(i).setText(Integer.toString(totals.get(i)));
            }


            // TotalValue.setText(Integer.toString(total));
            // dcTotalValue.setText(Integer.toString(dc));                   
            // imageTotalValue.setText(Integer.toString(image)); 
            // darkHorseTotalValue.setText(Integer.toString(darkHorse)); 
            // boomTotalValue.setText(Integer.toString(boom));
            // otherTotalValue.setText(Integer.toString(other));
            // xmenTotalValue.setText(Integer.toString(xmen));
            // marvelTotalValue.setText(Integer.toString(marvel));
            // seriesTotalValue.setText(Integer.toString(series));
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
                    if (item.getText().equals("Overview")){
                        month.setText("");
                        updateStats();
                    }
                    else{
                        month.setText(item.getText());
                        year.setVisible(true);
                        if (!year.getText().isEmpty()) {
                            updateStats();
                        }
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
