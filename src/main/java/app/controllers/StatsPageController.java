package app.controllers;

import java.util.ArrayList;

/**
 * Controller for the backlog tab
 *
 * @author Thomas Breimer
 * @version 5/22/23
 */

public class StatsPageController{

    private ArrayList<Integer> statList;

    /**
     * Assigns shared objects between mainScenesController
     * @param newBacklog backlog object
     * @param mainScenesController reference back to mainScenesController
     */
    public void setObjects(ArrayList<Integer> list) {
        statList=list;
    }

}