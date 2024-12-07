package app.controllers;

import java.io.IOException;
import java.util.ArrayList;

import app.DBConnection;
import app.MainScenesController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.KeyCode;
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
    @FXML private TreeView<String> titleTree;
    @FXML private Button searchButton;
    @FXML private AnchorPane pane;
    @FXML private Label totalIssues;
    private MainScenesController mainController;
    private Integer lastSearch;
    
    
    /**
     * Sets variables and creates any unmade javaFX features 
     */    
    public void setObjects(MainScenesController main) {
        mainController=main;
        lastSearch=-1;
        makeTitleTreeView();
        searchBySubstring();
        //mainController.makeTitlesButton(seriesTitles,seriesField);
        totalIssues.setVisible(false);        
        actionOnEnter();
    }
    
    /**
     * Searches the database for all issues of the designated series 
     * and displays them in a tree view
     * @param mouseEvent
     * @throws IOException
     */
    public TreeItem<String> searchIssues(@SuppressWarnings("exports") MouseEvent mouseEvent){
        TreeItem<String> selectedItem = titleTree.getSelectionModel().getSelectedItem();
        int level = titleTree.getTreeItemLevel(selectedItem);
        if (lastSearch!=-1){
            titleTree.getRoot().getChildren().get(lastSearch).getChildren().clear();
        }
        if (selectedItem==null || level!=1){
            mainController.errorMessage("No Series Selected", "Please select a series!");
        }
        else{
            createIssueView(selectedItem);
            totalIssues.setText("Issues Read: " +DBConnection.getNumIssuesSeries(selectedItem.getValue()));
            totalIssues.setVisible(true);
            lastSearch=titleTree.getRoot().getChildren().indexOf(selectedItem);
        }
        return selectedItem;
    }

    /**
     * Calls method of same name from mainController
     */
    public void updateTitles(){
        createTreeView();
    }

    /**
     * Adds an event handler to the series text 
     * field to search the Series table by substring.
     */
    private void searchBySubstring(){
        seriesField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent t) {
                //mainController.makeTitlesButton(seriesTitles,seriesField);
                createTreeView();
            }
        });
    }


    /**
     * Creates a tree view element and adds it to the AnchorPane
     */
    private void makeTitleTreeView(){
        titleTree=new TreeView<String>();
        titleTree.setLayoutX(33);
        titleTree.setLayoutY(30.0);
        titleTree.setPrefHeight(268.0);
        titleTree.setPrefWidth(355.0);
        pane.getChildren().add(1, titleTree);
        createTreeView();
    }

    /**
     * Creates a tree view that displays each issue of a series 
     * in the database along with the date it was read
     */
    private void createTreeView(){
        pane.getChildren().remove(titleTree);
        TreeItem<String> rootItem = new TreeItem<>("Issues");
        ArrayList<String> allTitles=DBConnection.getSeries();
        ArrayList<String> titles=new ArrayList<String>();
        for (int i=0;i<allTitles.size();i++){
            if (allTitles.get(i).contains(seriesField.getText())){
                titles.add(allTitles.get(i));
            }
        }
        for (int j = 0; j < titles.size(); j++) {
            String thisTitle = titles.get(j);
            TreeItem<String> treeTitle = new TreeItem<>(thisTitle);
            rootItem.getChildren().add(treeTitle);
        }
        titleTree.setRoot(rootItem);
        titleTree.setCellFactory(TextFieldTreeCell.forTreeView());
        titleTree.setShowRoot(false);
        pane.getChildren().add(titleTree);
    }

    private void createIssueView(TreeItem<String> title){
        ArrayList<ArrayList<String>> issues=DBConnection.getIssuesBySeriesName(title.getValue());
        // ArrayList<String> dates=DBConnection.getDatesBySeriesID(title.getValue());
        for (int i = 0; i < issues.size(); i++) {
            String thisIssue = issues.get(i).get(0);
            String thisDate = issues.get(i).get(1);
            TreeItem<String> treeIssue = new TreeItem<>(thisIssue+"    "+thisDate);
            title.getChildren().add(treeIssue);
        }
    }

    private void actionOnEnter(){
        seriesField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    searchIssues(null);
                }
            }
        });
        searchButton.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    searchIssues(null);
                }
            }
        });
        titleTree.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    TreeItem<String> selectedItem=searchIssues(null);
                    if (selectedItem!=null){
                        selectedItem.setExpanded(true);
                    }
                }
            }
        });
    }
}