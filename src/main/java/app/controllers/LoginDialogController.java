package app.controllers;

import app.*;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

/**
 * Controller for the login dialog
 *
 * @author Daniel Casper
 * @version 7/26/24
 */
public class LoginDialogController {

    @FXML private TextField urlField;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    
    public void setMain() {
        
    }

    public void login(MouseEvent mouseEvent) {
        String url=urlField.getText();
        String user=usernameField.getText();
        String password=passwordField.getText();
        DBConnection.setLogin(url,user,password);
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

}
