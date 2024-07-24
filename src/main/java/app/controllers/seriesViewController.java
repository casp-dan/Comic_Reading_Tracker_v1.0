package app.controllers;

import java.io.IOException;
import java.util.ArrayList;

import app.DBConnection;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * Controller for the series tree view tab page
 *
 * @author Daniel Casper
 * @version 7/13/24
 */

public class seriesViewController{

    @FXML private TextField seriesField;
    @FXML private MenuButton seriesTitles;
    @FXML private TreeView<String> issueTree;
    @FXML private AnchorPane pane;
    @FXML private Label totalIssues;




    /**
     * Sets variables and creates any unmade javaFX features 
     */    
    public void setObjects() {
        issueTree=new TreeView<String>();
        issueTree.setLayoutX(33);
        issueTree.setLayoutY(30.0);
        issueTree.setPrefHeight(268.0);
        issueTree.setPrefWidth(355.0);
        pane.getChildren().add(1, issueTree);
        seriesField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent t) {
                makeTitlesButton();
            }
        });
        makeTitlesButton();
        totalIssues.setLayoutX(150);
        totalIssues.setLayoutY(303);
        totalIssues.setVisible(false);        
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
     * Searches the database for all issues of the designated series 
     * and displays them in a table view
     * @param mouseEvent
     * @throws IOException
     */
    public void searchIssues(@SuppressWarnings("exports") MouseEvent mouseEvent) throws IOException {
        if (!DBConnection.getSeries().contains(seriesField.getText())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Series Does Not Exist");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid series title");
            alert.showAndWait();
        }
        else{
            createIssueView();
            totalIssues.setText("Issues Read: " +DBConnection.getNumIssues(seriesField.getText()));
            totalIssues.setVisible(true);
        }
    }

    /**
     * Creates a table view that displays each issue of a series 
     * in the database along with the date it was read
     */
    public void createIssueView(){
        pane.getChildren().remove(issueTree);
        TreeItem<String> rootItem = new CheckBoxTreeItem<>("Issues");
        ArrayList<String> issues=DBConnection.getIssuesBySeriesID(seriesField.getText());
        ArrayList<String> dates=DBConnection.getDatesBySeriesID(seriesField.getText());
        for (int i = 0; i < issues.size(); i++) {
            String thisIssue = issues.get(i);
            String thisDate = dates.get(i);
            TreeItem<String> treeIssue = new TreeItem<>(thisIssue+"    "+thisDate);
            rootItem.getChildren().add(treeIssue);
        }
        issueTree.setRoot(rootItem);
        issueTree.setCellFactory(TextFieldTreeCell.forTreeView());
        issueTree.setShowRoot(false);
        pane.getChildren().add(issueTree);
    }  
}