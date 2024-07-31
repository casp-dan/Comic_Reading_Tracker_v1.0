package app;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;

import java.io.IOException;
import app.controllers.MakeEntryDialogController;
import app.controllers.StatsController;
import app.controllers.dateViewController;
import app.controllers.LogoutController;
import app.controllers.seriesViewController;

/**
 * Main controller
 * @author Daniel Casper
 */

public class MainScenesController {

    @FXML private Parent makeEntryPage;
    @FXML private MakeEntryDialogController makeEntryPageController;
    
    @FXML private Parent seriesViewPage;
    @FXML private seriesViewController seriesViewPageController;
    
    @FXML private Parent dateViewPage;
    @FXML private dateViewController dateViewPageController;
    
    @FXML private Parent statsPage;
    @FXML private StatsController statsPageController;
    
    @FXML private Parent logoutPage;
    @FXML private LogoutController logoutPageController;
    

    
    /**
     * Called by App. Passes in object refs so they can be shared.
     */
    public void setObjects(App app) throws IOException {
        statsPageController.setObjects();
        dateViewPageController.setObjects(this);
        seriesViewPageController.setObjects(this);
        makeEntryPageController.setObjects(this);
        logoutPageController.assignObjects(app);
    }

    public void updateTabs(){
        statsPageController.updateStats();
        seriesViewPageController.makeTitlesButton();
    }

    /**
     * Creates and error message JavaFX alert with the given message.
     * @param title Title for the alert window
     * @param message Error message for the alert window
     */
    public void errorMessage(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}