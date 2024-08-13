package app.controllers;

import java.sql.Connection;

import app.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
    @FXML private Button loginButton;
    @FXML private PasswordField passwordField;

    public void setLoginButton(){
        loginAction();
    }
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

    /**
     * Adds an event handler to the series text 
     * field to search the Series table by substring.
     */
    private void loginAction(){
        loginButton.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    login(null);
                }
            }
        });
    }
}
