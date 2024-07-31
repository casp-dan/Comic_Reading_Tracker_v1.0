package app.controllers;

import java.io.IOException;
import java.util.ArrayList;

import app.DBConnection;
import app.MainScenesController;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import models.Date;

/**
 * Controller for the date view tab's page
 *
 * @author Daniel Casper
 * @version 7/13/24
 */

public class dateViewController{

    @FXML private TextField dateField;
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
        totalIssues.setVisible(false);        
    }

    /**
     * Searches the database for all issues with the indicated date 
     * and displays them in a tree view
     * @param mouseEvent
     * @throws IOException
     */
    public void searchIssues(@SuppressWarnings("exports") MouseEvent mouseEvent) throws IOException {
        Date search=new Date(dateField.getText());
        if (!search.toString().equals("0/0/0") && search.withinRange()) {
            if (createIssueView()){
                totalIssues.setText("Issues Read: "+ issueTree.getRoot().getChildren().size());
                totalIssues.setVisible(true);
            }
            else{
                mainController.errorMessage("Nothing Read", "You did not read any issues on this date!");
            }
        }
        else{
            mainController.errorMessage("Invalid Date", "Please Properly Enter Date!");
        }
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
     * Creates a tree view that displays each issue from a certain date
     * in the database in the format of seriesTitle #issueName
     */
    private boolean createIssueView(){
        pane.getChildren().remove(issueTree);
        TreeItem<String> rootItem = new CheckBoxTreeItem<>("Issues");
        ArrayList<String> issues=DBConnection.getIssuesByDate(dateField.getText());
        ArrayList<Integer> series=DBConnection.getSeriesByDate(dateField.getText());
        if (issues!=null){
            for (int i = 0; i < issues.size(); i++) {
                String thisSeries = DBConnection.getSeriesTitleByID(series.get(i));
                String thisIssue = issues.get(i);
                TreeItem<String> treeIssue = new TreeItem<>(thisSeries+" #"+thisIssue);
                rootItem.getChildren().add(treeIssue);
            }
            issueTree.setRoot(rootItem);
            issueTree.setCellFactory(TextFieldTreeCell.forTreeView());
            issueTree.setShowRoot(false);
            pane.getChildren().add(issueTree);
            return true;
        }
        else{
            pane.getChildren().add(issueTree);
            return false; 
        }
    }  

}