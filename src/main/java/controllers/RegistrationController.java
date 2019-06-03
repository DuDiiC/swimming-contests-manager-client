package controllers;

import dbModels.Club;
import dbModels.Competitor;
import dbModels.Contest;
import dbUtils.HibernateUtilClub;
import dbUtils.HibernateUtilContest;
import fxUtils.DialogsUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable {

    @FXML private ComboBox<Contest> contestComboBox;

    @FXML private ComboBox<Club> clubComboBox;

    @FXML private Button addAllCompetitorsButton;

    @FXML private Button deleteAllCompetitorsButton;

    @FXML private ComboBox<Competitor> competitorComboBox;

    @FXML private Button addCompetitorButton;

    @FXML private Button deleteCompetitorButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // contestComboBox
        ObservableList<Contest> contestList = FXCollections.observableArrayList();
        contestList.setAll(HibernateUtilContest.getAll());
        contestComboBox.setItems(contestList);

        // clubComboBox
        ObservableList<Club> clubList = FXCollections.observableArrayList();
        clubList.setAll(HibernateUtilClub.getAll());
        clubComboBox.setItems(clubList);
    }

    @FXML
    public void showCompetitors() {
        Club club = clubComboBox.getSelectionModel().getSelectedItem();
        if(club != null) {
            ObservableList<Competitor> competitorList = FXCollections.observableArrayList();
            competitorList.setAll(HibernateUtilClub.getAllCompetitors(club));
            competitorComboBox.setItems(competitorList);
        }
    }

    @FXML
    public void addAllCompetitorsFromClub() {
        if(contestComboBox.getSelectionModel().isEmpty() || clubComboBox.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz zawody i klub do zapisania zawodników!");
            return;
        }
        Contest contest = contestComboBox.getSelectionModel().getSelectedItem();
        List<Competitor> cListFromClub = clubComboBox.getSelectionModel().getSelectedItem().getCompetitors();
        List<Competitor> cList = contest.getCompetitors();
        List<Competitor> cListToAdd = new ArrayList<>();
        for(Competitor cFromClub : cListFromClub) {
            boolean isInContest = false;
            for(Competitor cFromContest : cList) {
                if(cFromContest.getPesel() == cFromClub.getPesel()) {
                    isInContest = true;
                    break;
                }
            }
            if(!isInContest) {
                cListToAdd.add(cFromClub);
            }
        }
        cList.addAll(cListToAdd);
        contest.setCompetitors(cList);
        HibernateUtilContest.addOrRemoveAllCompetitors(contest);
        // cleaning
        cleaningAfterRegistration();

    }

    @FXML
    public void removeAllCompetitorsFromClub() {
        if(contestComboBox.getSelectionModel().isEmpty() || clubComboBox.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz zawody i klub do wypisania zawodników!");
            return;
        }
        Contest contest = contestComboBox.getSelectionModel().getSelectedItem();
        Club club = clubComboBox.getSelectionModel().getSelectedItem();

        List<Competitor> actualList = contest.getCompetitors();
        List<Competitor> fromClubList = club.getCompetitors();
        for(int i = 0; i < fromClubList.size(); i++) {
            for(int j = 0; j < actualList.size(); j++) {
                if(fromClubList.get(i).getPesel() == actualList.get(j).getPesel()) {
                    actualList.remove(j);
                    break;
                }
            }
        }
        contest.setCompetitors(actualList);
        HibernateUtilContest.addOrRemoveAllCompetitors(contest);
        // cleaning
        cleaningAfterRegistration();
    }

    @FXML
    public void addCompetitor() {
        if(contestComboBox.getSelectionModel().isEmpty() || competitorComboBox.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz zawodnika do zapisania na zawody!");
            return;
        }
        Contest contest = contestComboBox.getSelectionModel().getSelectedItem();
        Competitor competitor = competitorComboBox.getSelectionModel().getSelectedItem();

        for(Competitor comp : contest.getCompetitors()) {
            if(comp.getPesel() == competitor.getPesel()) {
                DialogsUtil.errorDialog("Ten zawodnik jest już zapisany na te zawody!");
                cleaningAfterRegistration();
                return;
            }
        }

        List<Competitor> cList = contest.getCompetitors();
        cList.add(competitor);
        contest.setCompetitors(cList);

        HibernateUtilContest.addOrRemoveCompetitor(contest, competitor);

        // cleaning
        cleaningAfterRegistration();

    }

    @FXML
    public void removeCompetitor() {
        if(contestComboBox.getSelectionModel().isEmpty() || competitorComboBox.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz zawodnika do wypisania z zawodów!");
            return;
        }
        Contest contest = contestComboBox.getSelectionModel().getSelectedItem();
        Competitor competitor = competitorComboBox.getSelectionModel().getSelectedItem();

        List<Competitor> cList = contest.getCompetitors();
        boolean is = false;
        for(Competitor c : cList) {
            if(c.getPesel() == competitor.getPesel()) {
                cList.remove(competitor);
                is = true;
                break;
            }
        }
        if(is) {
            contest.setCompetitors(cList);
            HibernateUtilContest.addOrRemoveCompetitor(contest, competitor);
        } else {
            DialogsUtil.errorDialog("Ten zawodnik nie jest zapisany na te zawody!");
        }
        // cleaning
        cleaningAfterRegistration();
    }

    private void cleaningAfterRegistration() {
        clubComboBox.getSelectionModel().clearSelection();
        competitorComboBox.getSelectionModel().clearSelection();
        competitorComboBox.setItems(null);
        contestComboBox.getSelectionModel().clearSelection();
    }
}
