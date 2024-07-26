package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

import app.controllers.LoginDialogController;

/**
 * Main App Class
 * @author Daniel Casper
 */

public class App extends Application {

    @SuppressWarnings("exports")
    public Stage mainStage;
    private MainScenesController controller;
    public Stage loginStage;


    
    @SuppressWarnings("exports")
    public void start(Stage stage) throws IOException {
        try {

            FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("loginDialog.fxml"));
            Parent loginRoot = (Parent) loginLoader.load();
            LoginDialogController loginController = (LoginDialogController) loginLoader.getController();
            loginController.setMain();

            Scene loginScene = new Scene(loginRoot);
            loginStage = new Stage();

            loginStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
            loginStage.initModality(Modality.APPLICATION_MODAL);
            loginStage.setScene(loginScene);
            loginStage.setTitle("Login");
            loginStage.showAndWait();


            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainScenes.fxml"));
            Parent root = loader.load();
            controller = (MainScenesController) loader.getController();
            

            controller.setObjects(this);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(true);
            stage.setTitle("Comic Log™");
            DBConnection.connectDB();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));

            stage.show();

        } 
        catch(Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}