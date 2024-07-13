package app;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import app.controllers.MakeEntryDialogController;
import app.controllers.StatsPageController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import model.*;

/**
 * Main controller
 * @author Daniel CAsper
 */

public class MainScenesController implements Initializable {

    @FXML private Parent makeEntryPage;
    @FXML private MakeEntryDialogController makeEntryPageController;
    
    @FXML private Parent statsPage;
    @FXML private StatsPageController statsPageController;

    private ArrayList<Book> list;
    private ArrayList<Integer> statsList;
    
    private App mainApp;

    /**
     * Called by main. Passes in object refs so they can be shared.
     * @param backlog backlog object
     * @param sprint sprint object
     * @param archive archive object
     * @param classroom classroom object
     * @param mainApp ref back to main app
     * @param userRole "student" or "professor"
     * @param username name of student is userRole is "student"
     * @throws IOException 
     */
    public void setObjects() throws IOException {
        list = new ArrayList<Book>();
        statsList = new ArrayList<Integer>();
        makeEntryPageController.setObjects(statsPageController);
    }

    

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
