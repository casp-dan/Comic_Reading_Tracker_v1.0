package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Main Class
 * @author Daniel Casper
 */

public class App extends Application {

    private ArrayList<Book> list;
    private String userRole;
    private String username;
    public Stage mainStage;
    private MainScenesController controller;

    
    @Override
    public void start(Stage stage) throws IOException {
        try {


            this.list = new ArrayList<Book>();
            /** 
            // Login Window
            /** 
            // Login Window
            FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/app/makeEntryDialog.fxml"));
            Parent loginRoot = (Parent) loginLoader.load();
            MakeEntryDialogController loginController = (MakeEntryDialogController) loginLoader.getController();
            loginController.setMain(list);

            Scene loginScene = new Scene(loginRoot);
            mainStage = new Stage();

            mainStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
            mainStage.initModality(Modality.APPLICATION_MODAL);
            mainStage.setScene(loginScene);
            mainStage.setTitle("Comic Log");
            DBConnection.connectDB();

            
            mainStage.show();

            */
             // Main Window
             //this.sprint = new Sprint("Unnamed Sprint", 0);
             //this.archive = new SprintArchive(0);
             //this.classroom = new Classroom();
             
             FXMLLoader loader = new FXMLLoader(getClass().getResource("mainScenes.fxml"));
             Parent root = loader.load();
             controller = (MainScenesController) loader.getController();
             
             
             controller.setObjects(list, this);
             
             Scene scene = new Scene(root);
             stage.setScene(scene);
             stage.setResizable(true);
             stage.setTitle("Comic Log™");
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