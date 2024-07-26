package app.controllers;

import app.App;
import app.DBConnection;
import app.MainScenesController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Controller for the backlog tab
 *
 * @author Thomas Breimer
 * @version 5/22/23
 */

public class LogoutController{

    @FXML private Button logoutButton;
    private App app;


    /**
     * Assigns shared objects between mainScenesController
     * @param newBacklog backlog object
     * @param mainScenesController reference back to mainScenesController
     */
    public void assignObjects(App app) {
        this.app=app;
    }

    /**
     * When you want to delete either a task and its subtasks, or just a subtask, you click the delete selected button,
     * and it will delete what you have selected from the backlog
     * @param actionEvent for when you want to delete a selected task and its corresponding subtasks or just a single subtask
     * @throws java.io.IOException 
     * @throws IOException
     */
    @FXML
    public void logout(MouseEvent mouseEvent) throws java.io.IOException{
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
        DBConnection.setLogin(null, null, null);
        app.start(stage);
    }
}