package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Main App Class
 * @author Daniel Casper
 */

public class App extends Application {

    @SuppressWarnings("exports")
    public Stage mainStage;
    private MainScenesController controller;

    
    @SuppressWarnings("exports")
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainScenes.fxml"));
            Parent root = loader.load();
            controller = (MainScenesController) loader.getController();
            

            controller.setObjects();

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