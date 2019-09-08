package fxControllers;

import accessors.converters.ClubsConverter;
import accessors.converters.CompetitorsConverter;
import accessors.converters.ContestsConverter;
import fxUtils.DialogsUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import model.Club;
import model.Competitor;
import model.Contest;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Controller class for {RegistrationView.fxml} file.
 * Supports operations for registration {@link Competitor}s on {@link Contest}s.
 * That class implements @link Initializable interface used with fxControllers in JavaFX.
 */
public class RegistrationController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // set contestComboBox
        ObservableList<Contest> contestList = FXCollections.observableArrayList();
        List<Contest> contestBaseList = null;
        try {
            contestBaseList = contestsConverter.getAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        contestList.setAll(contestBaseList);
        contestComboBox.setItems(contestList);

        // set clubComboBox
        ObservableList<Club> clubList = FXCollections.observableArrayList();
        List<Club> clubBaseList = null;
        try {
            clubBaseList = clubsConverter.getAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        clubList.setAll(clubBaseList);
        clubComboBox.setItems(clubList);
    }

    /**
     * Called after selecting the {@link RegistrationController#clubComboBox} and showing all {@link Competitor}s
     * from selected {@link Club}.
     */
    @FXML public void setCompetitorsForSelectedClub() throws IOException {
        Club club = clubComboBox.getSelectionModel().getSelectedItem();
        ObservableList<Competitor> competitorList = FXCollections.observableArrayList();
        List<Competitor> competitorBaseList = competitorsConverter.getAllByClub(club.getId());
        competitorList.setAll(competitorBaseList);
        competitorComboBox.setItems(competitorList);
    }

    /**
     * Called after pressing the {@link RegistrationController#addAllCompetitorsButton} and adding correlation between
     * selected {@link Contest} and all {@link Competitor}s from selected {@link Club}.
     */
    @FXML public void addAllCompetitorsFromClub() throws IOException {

        // validate wrong data
        if(contestComboBox.getSelectionModel().isEmpty() || clubComboBox.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz zawody i klub do zapisania zawodników!");
            return;
        }

        // add competitors to set
        Contest selectedContest = contestComboBox.getSelectionModel().getSelectedItem();
        Club selectedClub = clubComboBox.getSelectionModel().getSelectedItem();

        List<Competitor> competitorFromSelectedClubList = competitorsConverter.getAllByClub(selectedClub.getId());
        Set<Competitor> allCompetitorSet = selectedContest.getCompetitors();
//        System.out.println(allCompetitorSet.size());
        allCompetitorSet.addAll(competitorFromSelectedClubList);
//        System.out.println(allCompetitorSet.size());
        selectedContest.setCompetitors(allCompetitorSet);

        // TODO: NIE DZIALA
        // update
        contestsConverter.update(selectedContest);

        // cleaning
        cleaningAfterRegistration();
    }

    /**
     * Called after pressing the {@link RegistrationController#removeAllCompetitorsButton} and removing correlation
     * between selected {@link Contest} and all {@link Competitor}s from selected {@link Club}.
     */
    @FXML public void removeAllCompetitorsFromClub() throws IOException {

        // validate wrong data
        if(contestComboBox.getSelectionModel().isEmpty() || clubComboBox.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz zawody i klub do wypisania zawodników!");
            return;
        }

        // remove competitors from set
        Contest selectedContest = contestComboBox.getSelectionModel().getSelectedItem();
        Club selectedClub = clubComboBox.getSelectionModel().getSelectedItem();

        List<Competitor> competitorFromSelectedClubList = competitorsConverter.getAllByClub(selectedClub.getId());
        Set<Competitor> allCompetitorSet = selectedContest.getCompetitors();
//        System.out.println(allCompetitorSet.size());
        allCompetitorSet.removeAll(competitorFromSelectedClubList);
//        System.out.println(allCompetitorSet.size());
        selectedContest.setCompetitors(allCompetitorSet);

        // TODO: NIE DZIALA
        // update
        contestsConverter.update(selectedContest);

        // cleaning
        cleaningAfterRegistration();
    }

    /**
     * Called after pressing the {@link RegistrationController#addCompetitorButton} and adding correlation between
     * selected {@link Contest} and selected {@link Competitor}.
     */
    @FXML public void addCompetitor() throws IOException {

        // validate wrong data
        if(contestComboBox.getSelectionModel().isEmpty() || competitorComboBox.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz zawodnika do zapisania na zawody!");
            return;
        }

        // add competitor to set
        Contest selectedContest = contestComboBox.getSelectionModel().getSelectedItem();
        Competitor selectedCompetitor = competitorComboBox.getSelectionModel().getSelectedItem();

        Set<Competitor> allCompetitorSet = selectedContest.getCompetitors();
        if(allCompetitorSet.contains(selectedCompetitor)) {
            DialogsUtil.errorDialog("Ten zawodnik jest już zapisany na te zawody!");
            contestComboBox.getSelectionModel().clearSelection();
            return;
        }
        allCompetitorSet.add(selectedCompetitor);
        selectedContest.setCompetitors(allCompetitorSet);

        // TODO: NIE DZIALA
        // update
        contestsConverter.update(selectedContest);

        // cleaning
        cleaningAfterRegistration();
    }

    /**
     * Called after pressing the {@link RegistrationController#removeCompetitorButton} between selected {@link Contest}
     * and selected {@link Competitor}.
     */
    @FXML public void removeCompetitor() throws IOException {

        // validate wrong data
        if(contestComboBox.getSelectionModel().isEmpty() || competitorComboBox.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz zawodnika do usunięcia z zawodów!");
            return;
        }

        // remove competitor from set
        Contest selectedContest = contestComboBox.getSelectionModel().getSelectedItem();
        Competitor selectedCompetitor = competitorComboBox.getSelectionModel().getSelectedItem();

        Set<Competitor> allCompetitorSet = selectedContest.getCompetitors();
        if(!allCompetitorSet.contains(selectedCompetitor)) {
            DialogsUtil.errorDialog("Ten zawodnik nie był zapisany na te zawody!");
            contestComboBox.getSelectionModel().clearSelection();
            return;
        }
        allCompetitorSet.remove(selectedCompetitor);
        selectedContest.setCompetitors(allCompetitorSet);

        // TODO: NIE DZIALA
        // update
        contestsConverter.update(selectedContest);

        // cleaning
        cleaningAfterRegistration();
    }

    /**
     * Clears all JavaFX elements after registration.
     */
    private void cleaningAfterRegistration() {
        clubComboBox.getSelectionModel().clearSelection();
        competitorComboBox.getSelectionModel().clearSelection();
        competitorComboBox.setItems(null);
        contestComboBox.getSelectionModel().clearSelection();
    }

    // --------------- VARIABLES --------------- //

    /**
     * {@link ComboBox} to select {@link Contest} object.
     */
    @FXML private ComboBox<Contest> contestComboBox;

    /**
     * {@link ComboBox} to select {@link Club} object.
     */
    @FXML private ComboBox<Club> clubComboBox;

    /**
     * {@link Button} to add correlation between all {@link Competitor}s from selected {@link Club}
     * and the selected {@link Contest}.
     */
    @FXML private Button addAllCompetitorsButton;

    /**
     * {@link Button} to remove correlation between all {@link Competitor}s from selected {@link Club}
     * and the selected {@link Contest}.
     */
    @FXML private Button removeAllCompetitorsButton;

    /**
     * {@link ComboBox} to select {@link Competitor} object from selected {@link Club}.
     */
    @FXML private ComboBox<Competitor> competitorComboBox;

    /**
     * {@link Button} to add correlation between selected {@link Competitor} and selected {@link Contest}.
     */
    @FXML private Button addCompetitorButton;

    /**
     * {@link Button} to remove correlation betweend selected {@link Competitor} and selected {@link Contest}.
     */
    @FXML private Button removeCompetitorButton;

    private ContestsConverter contestsConverter = new ContestsConverter();
    private ClubsConverter clubsConverter = new ClubsConverter();
    private CompetitorsConverter competitorsConverter = new CompetitorsConverter();

}
