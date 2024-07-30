package app.controllers;

import app.App;
import app.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Controller for the logout tab
 *
 * @author Daniel Casper
 * @version 7/26/24
 */

public class LogoutController{

    @FXML private Button logoutButton;
    private App app;


    /**
     * Assigns app object to reset the stage
     * @param app main app object
     */
    public void assignObjects(App app) {
        this.app=app;
    }

    /**
     * Return to login scene and clear login info from DBConnection
     * @param MouseEvent triggers method when the logout button is clicked
     * @throws java.io.IOException 
     */
    @FXML
    public void logout(@SuppressWarnings("exports") MouseEvent mouseEvent) throws java.io.IOException{
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
        DBConnection.setLogin(null, null);
        app.start(stage);
    }
}