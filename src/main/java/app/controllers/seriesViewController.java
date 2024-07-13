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
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Entry;

/**
 * Controller for the backlog tab
 *
 * @author Thomas Breimer
 * @version 5/22/23
 */

public class seriesViewController{

    @FXML private TextField seriesField;
    @FXML private MenuButton seriesTitles;
    @FXML private TreeView<String> issueTree;
    @FXML private AnchorPane pane;




    /**
     * Assigns shared objects between mainScenesController
     * @param newBacklog backlog object
     * @param mainScenesController reference back to mainScenesController
     */
    public void setObjects() {
        issueTree=new TreeView<String>();
        issueTree.setLayoutX(33);
        issueTree.setLayoutY(30.0);
        issueTree.setPrefHeight(284.0);
        issueTree.setPrefWidth(355.0);
        pane.getChildren().add(1, issueTree);
        seriesField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent t) {
                makeTitlesButton();
            }
        });
        makeTitlesButton();
    }

    public void makeTitlesButton(){
        ObservableList<MenuItem> bookNames=seriesTitles.getItems();
        bookNames.clear();
        MenuItem item1=new MenuItem("");
        item1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                seriesField.setText("");
                seriesTitles.setText("");
            }
        });
        bookNames.add(item1);
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

    public void searchIssues(@SuppressWarnings("exports") MouseEvent mouseEvent) throws IOException {
        if (!DBConnection.getSeries().contains(seriesField.getText())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Series Does Not Exist");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid series title");
            alert.showAndWait();
        }
        else{
            createSprintArchiveTreeView();
        }
    }

    public void createSprintArchiveTreeView(){
        pane.getChildren().remove(issueTree);
        TreeItem<String> rootItem = new CheckBoxTreeItem<>("Issues");
        ArrayList<String> issues=DBConnection.getIssuesBySeriesID(seriesField.getText());
        ArrayList<String> dates=DBConnection.getDatesBySeriesID(seriesField.getText());
        for (int i = 0; i < issues.size(); i++) {
            String thisIssue = issues.get(i);
            String thisDate = dates.get(i);
            TreeItem<String> treeIssue = new TreeItem<>(thisIssue+":     "+thisDate);
            rootItem.getChildren().add(treeIssue);
        }
        issueTree.setRoot(rootItem);
        issueTree.setCellFactory(TextFieldTreeCell.forTreeView());
        issueTree.setShowRoot(false);
        pane.getChildren().add(issueTree);
    }  
}