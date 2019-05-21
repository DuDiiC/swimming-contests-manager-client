package controllers;

import fxUtils.FxmlUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;


public class MainBorderPaneController {

    private static final String CLUB_VIEW_FXML = "/fxml/ClubView.fxml";
    private static final String CONTEST_VIEW_FXML = "/fxml/ContestView.fxml";
    private static final String COMPETITOR_VIEW_FXML = "/fxml/CompetitorView.fxml";
    private static final String TRAINER_VIEW_FXML = "/fxml/TrainerView.fxml";
    private static final String COMPETITION_VIEW_FXML = "/fxml/CompetitionView.fxml";
    private static final String RECORD_VIEW_FXML = "/fxml/RecordView.fxml";
    private static final String REGISTRATION_VIEW_FXML = "/fxml/RegistrationView.fxml";

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
        setCenter(COMPETITION_VIEW_FXML);
    }

    @FXML
    public void viewCompetitors() {
        setCenter(COMPETITOR_VIEW_FXML);
    }

    @FXML
    public void viewRecords() {
        setCenter(RECORD_VIEW_FXML);
    }

    @FXML
    public void viewClubs() {
        setCenter(CLUB_VIEW_FXML);
    }

    @FXML
    public void viewTrainers() {
        setCenter(TRAINER_VIEW_FXML);
    }

    @FXML
    public void viewRegistration() {
        setCenter(REGISTRATION_VIEW_FXML);
    }

    private void setCenter(String fxmlPath) {
        borderPane.setCenter(FxmlUtil.fxmlLoader(fxmlPath));
    }
}
