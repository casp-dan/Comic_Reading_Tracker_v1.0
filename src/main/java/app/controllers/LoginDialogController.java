package app.controllers;

import java.sql.Connection;

import app.*;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controller for the login dialog
 *
 * @author Daniel Casper
 * @version 7/26/24
 */
public class LoginDialogController {

    @FXML private TextField urlField;
    @FXML private PasswordField passwordField;

    /**
     * Called when the login button is clicked
     * @param mouseEvent
     */
    public void login(@SuppressWarnings("exports") MouseEvent mouseEvent) {
        String password=passwordField.getText();
        String url=urlField.getText();
        DBConnection.setLogin(url,password);
        Connection connect=DBConnection.connectDB();
        if (connect!=null){
            Stage stage = (Stage) urlField.getScene().getWindow();
            stage.close();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid Credentials");
            alert.setHeaderText(null);
            alert.setContentText("Invalid credentials. Please enter a valid url and password.");
            alert.showAndWait();
            urlField.setText("");
            passwordField.setText("");
        }
    }
}
