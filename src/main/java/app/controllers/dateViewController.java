package app.controllers;

import java.io.IOException;
import java.util.ArrayList;

import app.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * Controller for the series tree view tab page
 *
 * @author Daniel Casper
 * @version 7/13/24
 */

public class dateViewController{

    @FXML private TextField dateField;
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
        totalIssues.setLayoutX(150);
        totalIssues.setLayoutY(303);
        totalIssues.setVisible(false);        
    }

    /**
     * Searches the database for all issues of the designated series 
     * and displays them in a table view
     * @param mouseEvent
     * @throws IOException
     */
    public void searchIssues(@SuppressWarnings("exports") MouseEvent mouseEvent) throws IOException {
        if (properDate(dateField.getText())){
            if (createIssueView()){
                totalIssues.setText("Issues Read: "+ issueTree.getRoot().getChildren().size());
                totalIssues.setVisible(true);
            }
            else{
                errorMessage("Nothing Read", "You did not read any issues on this date!");
            }
        }
    }

    /**
     * Creates a table view that displays each issue of a series 
     * in the database along with the date it was read
     */
    public boolean createIssueView(){
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
    
    /**
     * Verifies that the contents of the date Text Field contains a valid date in the proper form of mm/dd/yyyy.
     * Years are stored only by the last 2 digits (ie: 2024-->24) and years entered as 2 digits will be accepted as well. 
     * @param dateString Text string retrieved from the date text field
     * @return true if date is properly formatted, false if not 
     */
    private boolean properDate(String dateString){
        String message="Please Properly Enter Date!";
        String title="Invalid Date";
        String[] date=dateString.split("/");
        java.time.LocalDate ldt =java.time.LocalDate.now();
            String[] today=ldt.toString().split("-");
            if (Integer.parseInt(today[1])<10){
                today[1]=today[1].split("0")[1];
            }
            today[0]=today[0].split("0")[1];
        if (date.length!=3){
            errorMessage(title, message);
            return false;
        }
        else if (Integer.parseInt(date[2])>=22){
            if (Integer.parseInt(date[2])==Integer.parseInt(today[0])){
                if (Integer.parseInt(date[0])>Integer.parseInt(today[1])){
                    errorMessage(title, message);
                    return false;
                }
                else if (Integer.parseInt(date[1])>Integer.parseInt(today[2])){
                    errorMessage(title, message);
                    return false;
                }
            }
            else if (Integer.parseInt(date[2])>Integer.parseInt(today[0])){
                errorMessage(title, message);
                return false;
            }
            else{
                return true;
            }
        }
        errorMessage(title, message);
        return false;

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

}