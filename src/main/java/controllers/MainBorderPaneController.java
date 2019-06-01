package controllers;

import dbUtils.HibernateUtil;
import fxUtils.DialogsUtil;
import fxUtils.FxmlUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import javax.persistence.PersistenceException;
import java.net.URL;
import java.util.ResourceBundle;


public class MainBorderPaneController implements Initializable {

    private static final String CLUB_VIEW_FXML = "/fxml/ClubView.fxml";
    private static final String CONTEST_VIEW_FXML = "/fxml/ContestView.fxml";
    private static final String COMPETITOR_VIEW_FXML = "/fxml/CompetitorView.fxml";
    private static final String TRAINER_VIEW_FXML = "/fxml/TrainerView.fxml";
    private static final String COMPETITION_VIEW_FXML = "/fxml/CompetitionView.fxml";
    private static final String REGISTRATION_VIEW_FXML = "/fxml/RegistrationView.fxml";
    private static final String WELCOME_VIEW_FXML = "/fxml/WelcomeView.fxml";

    @FXML private BorderPane borderPane;

    @FXML private ToggleGroup tables;

    @FXML private ToggleButton contestsButton;

    @FXML private ToggleButton registrationButton;

    @FXML private ToggleButton competitionsButton;

    @FXML private ToggleButton clubsButton;

    @FXML private ToggleButton trainersButton;

    @FXML private ToggleButton competitorsButton;

    @FXML private ImageView exitButton;

    @FXML private TextField loginTextField;

    @FXML private PasswordField passwordTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setButtonsDisable(true);
    }

    @FXML
    private void logIn() {
        String user = loginTextField.getText();
        String passwd = passwordTextField.getText();

        try {
            HibernateUtil.setEm(HibernateUtil.createEM(user, passwd));
        } catch (PersistenceException e) {
            DialogsUtil.errorDialog("Niepoprawna nazwa użytkownika lub hasło!");
            loginTextField.clear();
            passwordTextField.clear();
            return;
        }

        DialogsUtil.informationDialog("Logowanie zakończone sukcesem");

        setButtonsDisable(false);

        setCenter(WELCOME_VIEW_FXML);
    }

    private void setButtonsDisable(boolean choice) {
        contestsButton.setDisable(choice);
        registrationButton.setDisable(choice);
        competitionsButton.setDisable(choice);
        clubsButton.setDisable(choice);
        trainersButton.setDisable(choice);
        competitionsButton.setDisable(choice);
        competitorsButton.setDisable(choice);
    }

    @FXML
    public void viewContests() {
        if(contestsButton.isSelected()) {
            setCenter(CONTEST_VIEW_FXML);
        } else {
            setCenter(WELCOME_VIEW_FXML);
        }
    }

    @FXML
    public void viewRegistration() {
        if(registrationButton.isSelected()) {
            setCenter(REGISTRATION_VIEW_FXML);
        } else {
            setCenter(WELCOME_VIEW_FXML);
        }
    }

    @FXML
    public void viewCompetitions() {
        if(competitionsButton.isSelected()) {
            setCenter(COMPETITION_VIEW_FXML);
        } else {
            setCenter(WELCOME_VIEW_FXML);
        }
    }

    @FXML
    public void viewClubs() {
        if(clubsButton.isSelected()) {
            setCenter(CLUB_VIEW_FXML);
        } else {
            setCenter(WELCOME_VIEW_FXML);
        }
    }

    @FXML
    public void viewTrainers() {
        if(trainersButton.isSelected()) {
            setCenter(TRAINER_VIEW_FXML);
        } else {
            setCenter(WELCOME_VIEW_FXML);
        }
    }

    @FXML
    public void viewCompetitors() {
        if(competitorsButton.isSelected()) {
            setCenter(COMPETITOR_VIEW_FXML);
        } else {
            setCenter(WELCOME_VIEW_FXML);
        }
    }

    @FXML
    public void exit() {
        if(HibernateUtil.getEm() != null) HibernateUtil.getEm().close();
        Platform.exit();
    }

    private void setCenter(String fxmlPath) {
        borderPane.setCenter(FxmlUtil.fxmlLoader(fxmlPath));
    }
}
