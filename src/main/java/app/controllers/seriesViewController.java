package app.controllers;

import java.io.IOException;
import java.util.ArrayList;

import app.DBConnection;
import app.MainScenesController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
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
    private MainScenesController mainController;
    
    
    /**
     * Sets variables and creates any unmade javaFX features 
     */    
    public void setObjects(MainScenesController main) {
        mainController=main;
        makeIssueTreeView();
        searchBySubstring();
        mainController.makeTitlesButton(seriesTitles,seriesField);
        totalIssues.setVisible(false);        
    }
    
    /**
     * Searches the database for all issues of the designated series 
     * and displays them in a tree view
     * @param mouseEvent
     * @throws IOException
     */
    public void searchIssues(@SuppressWarnings("exports") MouseEvent mouseEvent) throws IOException {
        if (!DBConnection.getSeries().contains(seriesField.getText())){
            mainController.errorMessage("Series Does Not Exist", "Please enter a valid series title");
        }
        else{
            createIssueView();
            totalIssues.setText("Issues Read: " +DBConnection.getNumIssuesSeries(seriesField.getText()));
            totalIssues.setVisible(true);
        }
    }
    
    /**
     * Calls method of same name from mainController
     */
    public void makeTitlesButton(){
        mainController.makeTitlesButton(seriesTitles,seriesField);
    }

    /**
     * Adds an event handler to the series text 
     * field to search the Series table by substring.
     */
    private void searchBySubstring(){
        seriesField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent t) {
                mainController.makeTitlesButton(seriesTitles,seriesField);
            }
        });
    }


    /**
     * Creates a tree view element and adds it to the AnchorPane
     */
    private void makeIssueTreeView(){
        issueTree=new TreeView<String>();
        issueTree.setLayoutX(33);
        issueTree.setLayoutY(30.0);
        issueTree.setPrefHeight(268.0);
        issueTree.setPrefWidth(355.0);
        pane.getChildren().add(1, issueTree);
    }

    /**
     * Creates a tree view that displays each issue of a series 
     * in the database along with the date it was read
     */
    private void createIssueView(){
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