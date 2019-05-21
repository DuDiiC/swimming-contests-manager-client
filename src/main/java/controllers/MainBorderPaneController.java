package controllers;

import fxUtils.FxmlUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;


public class MainBorderPaneController {

    @FXML private BorderPane borderPane;

    @FXML private ToggleGroup tables;

    @FXML private ToggleButton contestsButton;

    @FXML private ToggleButton competitionsButton;

    @FXML private ToggleButton competitorsButton;

    @FXML private ToggleButton recordsButton;

    @FXML private ToggleButton clubsButton;

    @FXML private ToggleButton trainersButton;

    @FXML
    public void viewContests() {
        setCenter(CONTEST_VIEW_FXML);
    }

    @FXML
    public void viewCompetitions() {
    }

    @FXML
    public void viewCompetitors() {
    }

    @FXML
    public void viewRecords() {
    }

    @FXML
    public void viewClubs() {
    }

    @FXML
    public void viewTrainers() {
    }

    @FXML
    public void viewRegistration() {
    }

    private void setCenter(String fxmlPath) {
    }
}
