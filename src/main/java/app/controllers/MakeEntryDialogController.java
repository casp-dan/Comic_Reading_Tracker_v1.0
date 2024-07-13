package app.controllers;

import java.io.IOException;
import java.util.ArrayList;

import app.App;
import app.DBConnection;
import model.*;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * Controller for making a new entry
 *
 * @author Daniel Casper
 */
public class MakeEntryDialogController {

    @FXML private AnchorPane pane;
    @FXML private MenuButton publisher;

    private StatsPageController stats;
    private CheckBox xmen;

    @FXML public TextField seriesField;
    @FXML public TextField dateField;
    @FXML public TextField issuesField; 
    @FXML private Label TotalValue;
    @FXML private Label marvelTotalValue;
    @FXML private Label dcTotalValue;
    @FXML private Label imageTotalValue;
    @FXML private Label xmenTotalValue;

    //private Authenticator authenticator;

    public void setObjects(StatsPageController newStats) {
        updateView();
        xmen=new CheckBox("X-Men?");
        xmen.setLayoutX(100);
        xmen.setVisible(false);
        pane.getChildren().add(1, xmen);
        makePublisherButton();
    }

    /**
     * Called when login as student is clicked
     * @param mouseEvent
     * @throws IOException 
     */
    public void makeEntry(MouseEvent mouseEvent) throws IOException {
        Entry entry=new Entry(seriesField.getText(), issuesField.getText(), dateField.getText(), publisher.getText(), xmen.isSelected());
        updateView();
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
        TotalValue.setText(Integer.toString(DBConnection.getTotal()));
        marvelTotalValue.setText(Integer.toString(DBConnection.getNumPublisher("Marvel")));
        dcTotalValue.setText(Integer.toString(DBConnection.getNumPublisher("DC")));
        imageTotalValue.setText(Integer.toString(DBConnection.getNumPublisher("Image")));
        xmenTotalValue.setText(Integer.toString(DBConnection.getNumXMen()));
    }

}

