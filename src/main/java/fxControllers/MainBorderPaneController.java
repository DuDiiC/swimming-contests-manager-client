package fxControllers;

import fxUtils.FxmlUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main controller class which sets all others layouts.
 * That class implements {@link Initializable} interface used with fxControllers in JavaFX.
 */
public class MainBorderPaneController implements Initializable {

    private static final String CLUB_VIEW_FXML = "/fxml/ClubView.fxml";
    private static final String CONTEST_VIEW_FXML = "/fxml/ContestView.fxml";
    private static final String COMPETITOR_VIEW_FXML = "/fxml/CompetitorView.fxml";
    private static final String TRAINER_VIEW_FXML = "/fxml/TrainerView.fxml";
    private static final String COMPETITION_VIEW_FXML = "/fxml/CompetitionView.fxml";
    private static final String REGISTRATION_VIEW_FXML = "/fxml/RegistrationView.fxml";
    private static final String WELCOME_VIEW_FXML = "/fxml/WelcomeView.fxml";

    /**
     * {@link BorderPane} where application displays every other layouts.
     */
    @FXML private BorderPane borderPane;

    /**
     * {@link ToggleGroup} for all menu options.
     */
    @FXML private ToggleGroup tables;

    /**
     * {@link ToggleButton} for displaying contest layout.
     */
    @FXML private ToggleButton contestsButton;

    /**
     * {@link ToggleButton} for displaying registration layout.
     */
    @FXML private ToggleButton registrationButton;

    /**
     * {@link ToggleButton} for displaying competition layout.
     */
    @FXML private ToggleButton competitionsButton;

    /**
     * {@link ToggleButton} for displaying club layout.
     */
    @FXML private ToggleButton clubsButton;

    /**
     * {@link ToggleButton} for displaying trainer layout.
     */
    @FXML private ToggleButton trainersButton;

    /**
     * {@link ToggleButton} for displaying competitor layout.
     */
    @FXML private ToggleButton competitorsButton;

    /**
     * {@link ImageView} used for close application and database connection.
     */
    @FXML private ImageView exitButton;

    /**
     * {@link TextField} for user's login.
     */
    @FXML private TextField loginTextField;

    /**
     * {@link TextField} for user's psasword.
     */
    @FXML private PasswordField passwordTextField;

    /**
     * {@link Button} to login to the application and the database.
     */
    @FXML private Button loginButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setButtonsDisable(true);
    }

    /**
     * Called after pressing {@link MainBorderPaneController#loginButton}.
     * Checks user's login and password and gives access to the application if are correct.
     */
    @FXML private void logIn() {

        // personal information about user
//        String user = loginTextField.getText();
//        String password = passwordTextField.getText();

//        if(!(user.equals("admin") && password.equals("123"))) {
//            DialogsUtil.errorDialog("Niepoprawna nazwa użytkownika lub hasło!");
//            loginTextField.clear();
//            passwordTextField.clear();
//            return;
//        }

//        DialogsUtil.informationDialog("Logowanie zakończone sukcesem");

        setButtonsDisable(false);

        setCenter(WELCOME_VIEW_FXML);
    }

    /**
     * Sets whether the all {@link ToggleButton}s are active or inactive.
     * @param choice if true the all {@link ToggleButton}s are active, in other case inactive.
     */
    private void setButtonsDisable(boolean choice) {
        contestsButton.setDisable(choice);
        registrationButton.setDisable(choice);
        competitionsButton.setDisable(choice);
        clubsButton.setDisable(choice);
        trainersButton.setDisable(choice);
        competitionsButton.setDisable(choice);
        competitorsButton.setDisable(choice);
    }

    /**
     * Called after pressing {@link MainBorderPaneController#contestsButton} and showing contest layout.
     */
    @FXML public void viewContests() {
        if(contestsButton.isSelected()) {
            setCenter(CONTEST_VIEW_FXML);
        } else {
            setCenter(WELCOME_VIEW_FXML);
        }
    }

    /**
     * Called after pressing {@link MainBorderPaneController#registrationButton} and showing registration layout.
     */
    @FXML public void viewRegistration() {
        if(registrationButton.isSelected()) {
            setCenter(REGISTRATION_VIEW_FXML);
        } else {
            setCenter(WELCOME_VIEW_FXML);
        }
    }

    /**
     * Called after pressing {@link MainBorderPaneController#competitionsButton} and showing competition layout.
     */
    @FXML public void viewCompetitions() {
        if(competitionsButton.isSelected()) {
            setCenter(COMPETITION_VIEW_FXML);
        } else {
            setCenter(WELCOME_VIEW_FXML);
        }
    }

    /**
     * Called after pressing {@link MainBorderPaneController#clubsButton} and showing club layout.
     */
    @FXML public void viewClubs() {
        if(clubsButton.isSelected()) {
            setCenter(CLUB_VIEW_FXML);
        } else {
            setCenter(WELCOME_VIEW_FXML);
        }
    }

    /**
     * Called after pressing {@link MainBorderPaneController#trainersButton} and showing trainer layout.
     */
    @FXML public void viewTrainers() {
        if(trainersButton.isSelected()) {
            setCenter(TRAINER_VIEW_FXML);
        } else {
            setCenter(WELCOME_VIEW_FXML);
        }
    }

    /**
     * Called after pressing {@link MainBorderPaneController#competitorsButton} and showing competitor layout.
     */
    @FXML public void viewCompetitors() {
        if(competitorsButton.isSelected()) {
            setCenter(COMPETITOR_VIEW_FXML);
        } else {
            setCenter(WELCOME_VIEW_FXML);
        }
    }

    /**
     * Called after pressing {@link MainBorderPaneController#exitButton}.
     * Close hibernate connection and application.
     */
    @FXML public void exit() {
//        if(HibernateUtil.getEm() != null) HibernateUtil.getEm().close();
        Platform.exit();
    }

    /**
     * Sets layout in {@link String} format on the center of {@link MainBorderPaneController#borderPane}.
     * @param fxmlPath path to the FXML layout in {@link String} format.
     */
    private void setCenter(String fxmlPath) {
        borderPane.setCenter(FxmlUtil.fxmlLoader(fxmlPath));
    }
}
