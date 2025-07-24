package app;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;

import app.controllers.MakeEntryDialogController;
import app.controllers.StatsController;
import app.controllers.dateViewController;
import app.controllers.LogoutController;
import app.controllers.seriesViewController;

/**
 * Main controller
 * @author Daniel Casper
 */

public class MainScenesController {

    @FXML private TabPane tabs;

    @FXML private Parent makeEntryPage;
    @FXML private MakeEntryDialogController makeEntryPageController;
    
    @FXML private Parent seriesViewPage;
    @FXML private seriesViewController seriesViewPageController;
    
    @FXML private Parent dateViewPage;
    @FXML private dateViewController dateViewPageController;
    
    @FXML private Parent statsPage;
    @FXML private StatsController statsPageController;
    
    @FXML private Parent logoutPage;
    @FXML private LogoutController logoutPageController;
    
    public double height;
    
    /**
     * Called by App. Passes in object refs so they can be shared.
     */
    public void setObjects(App app) throws IOException {
        double h=tabs.getPrefHeight();
        height=h+=DBConnection.getPublishers().size()*42;
        tabs.setPrefHeight(height);
        statsPageController.setObjects(this);
        dateViewPageController.setObjects(this);
        seriesViewPageController.setObjects(this);
        makeEntryPageController.setObjects(this);
        logoutPageController.assignObjects(app);
    }
    
    public void updateTabs(){
        statsPageController.makePage();
        statsPageController.updateStats();
        seriesViewPageController.updateTitles();
    }

    /**
     * Creates and error message JavaFX alert with the given message.
     * @param title Title for the alert window
     * @param message Error message for the alert window
     */
    public void errorMessage(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Creates a dropdown menu button to select the title of a series that already exists.
     */
    public void makeTitlesButton(@SuppressWarnings("exports") MenuButton seriesTitles, @SuppressWarnings("exports") TextField seriesField, Integer scene){
        ObservableList<MenuItem> bookNames=seriesTitles.getItems();
        bookNames.clear();
        ArrayList<String> titles;
        if (scene==1){
            titles=DBConnection.getSeries();
        }
        else{
            titles=DBConnection.getCollectedSeries();
        }
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

    @SuppressWarnings("exports")
    public Separator makeSeparator(){
        Separator sep=new Separator();
        sep.setOrientation(Orientation.VERTICAL);
        sep.setPrefHeight(0);
        sep.setPrefWidth(6.0);
        sep.setVisible(false);
        return sep;
    }

}