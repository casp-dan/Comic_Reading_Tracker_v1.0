package app.controllers;

import java.sql.Connection;

import app.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.collections.ObservableList;


/**
 * Controller for the login dialog
 *
 * @author Daniel Casper
 * @version 7/26/24
 */
public class LoginDialogController {

    @FXML private TextField urlField;
    @FXML private AnchorPane pane;
    @FXML private Button loginButton;
    @FXML private PasswordField passwordField;
    @FXML private VBox box;
    private TextField visiblePassword;
    private CheckBox visibility;

    public void setLoginButton(){
        loginAction();
        visiblePassword=new TextField();
        visiblePassword.setLayoutX(26.5);
        visiblePassword.setLayoutY(106.5);
        visiblePassword.setVisible(false);
        pane.getChildren().add(visiblePassword);
        makeShowPassword();

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
            //urlField.setText("");
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
        urlField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    login(null);
                }
            }
        });
        passwordField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    login(null);
                }
            }
        });
    }

    private void makeShowPassword(){
        visibility=new CheckBox("Show Password");
        visibility.setLayoutY(106.5);
        visibility.setLayoutX(26.5);
        visibility.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent t) {
                if (visibility.isSelected()){
                    visiblePassword.setVisible(true);
                    visiblePassword.setText(passwordField.getText());
                    passwordField.setVisible(false);
                }
                else{
                    visiblePassword.setVisible(false);
                    passwordField.setText(visiblePassword.getText());

                    passwordField.setVisible(true);
                }
            }
        });
        box.getChildren().add((box.getChildren().size())-2,visibility);
    }
}
