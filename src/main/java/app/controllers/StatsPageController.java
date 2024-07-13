package app.controllers;

import app.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller for the backlog tab
 *
 * @author Thomas Breimer
 * @version 5/22/23
 */

public class StatsPageController{

    @FXML private Label TotalValue;
    @FXML private Label marvelTotalValue;
    @FXML private Label dcTotalValue;
    @FXML private Label imageTotalValue;
    @FXML private Label xmenTotalValue;

    /**
     * Assigns shared objects between mainScenesController
     * @param newBacklog backlog object
     * @param mainScenesController reference back to mainScenesController
     */
    public void setObjects() {
        
    }

    public void updateView(){
        TotalValue.setText(Integer.toString(DBConnection.getTotal()));
        marvelTotalValue.setText(Integer.toString(DBConnection.getNumPublisher("Marvel")));
        dcTotalValue.setText(Integer.toString(DBConnection.getNumPublisher("DC")));
        imageTotalValue.setText(Integer.toString(DBConnection.getNumPublisher("Image")));
        xmenTotalValue.setText(Integer.toString(DBConnection.getNumXMen()));
    }

}