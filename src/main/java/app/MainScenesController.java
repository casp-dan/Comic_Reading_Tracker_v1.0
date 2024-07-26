package app;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import java.io.IOException;
import app.controllers.MakeEntryDialogController;
import app.controllers.StatsController;
import app.controllers.dateViewController;
import app.controllers.LogoutController;
import app.controllers.seriesViewController;

/**
 * Main controller
 * @author Daniel CAsper
 */

public class MainScenesController {

    @FXML private Parent makeEntryPage;
    @FXML private MakeEntryDialogController makeEntryPageController;
    
    @FXML private Parent seriesViewPage;
    @FXML private seriesViewController seriesViewPageController;
    
    @FXML private Parent dateViewPage;
    @FXML private dateViewController dateViewPageController;
    
    @FXML private Parent logoutPage;
    @FXML private LogoutController logoutPageController;
    
    @FXML private Parent statsPage;
    @FXML private StatsController statsPageController;

    
    /**
     * Called by App. Passes in object refs so they can be shared.
     */
    public void setObjects(App app) throws IOException {
        statsPageController.setObjects();
        dateViewPageController.setObjects();
        makeEntryPageController.setObjects(statsPageController);
        seriesViewPageController.setObjects();
        logoutPageController.assignObjects(app);
    }

}