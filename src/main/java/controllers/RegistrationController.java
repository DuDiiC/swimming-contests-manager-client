package controllers;

import dbModels.Club;
import dbModels.Competitor;
import dbModels.Contest;
import dbUtils.HibernateUtilClub;
import dbUtils.HibernateUtilContest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.net.URL;
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
        ObservableList<Competitor> competitorList = FXCollections.observableArrayList();
        competitorList.setAll(HibernateUtilClub.getAllCompetitors(club));
        competitorComboBox.setItems(competitorList);
    }

    @FXML
    public void addAllCompetitorsFromClub() {
        Contest contest = contestComboBox.getSelectionModel().getSelectedItem();
        List<Competitor> cListFromClub = clubComboBox.getSelectionModel().getSelectedItem().getCompetitors();
        List<Competitor> cList = contest.getCompetitors();
        cList.addAll(cListFromClub);
        contest.setCompetitors(cList);

        HibernateUtilContest.addOrRemoveAllCompetitors(contest);
    }

    @FXML
    public void removeAllCompetitorsFromClub() {
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

        // clearing

        // refresh view
    }

    @FXML
    public void addCompetitor() {
        Contest contest = contestComboBox.getSelectionModel().getSelectedItem();
        Competitor competitor = competitorComboBox.getSelectionModel().getSelectedItem();

        List<Competitor> cList = contest.getCompetitors();
        cList.add(competitor);
        contest.setCompetitors(cList);

        HibernateUtilContest.addOrRemoveCompetitor(contest, competitor);
    }

    @FXML
    public void removeCompetitor() {
        Contest contest = contestComboBox.getSelectionModel().getSelectedItem();
        Competitor competitor = competitorComboBox.getSelectionModel().getSelectedItem();

        List<Competitor> cList = contest.getCompetitors();
        for(Competitor c : cList) {
            if(c.getPesel() == competitor.getPesel()) {
                cList.remove(competitor);
                break;
            }
        }
        contest.setCompetitors(cList);

        HibernateUtilContest.addOrRemoveCompetitor(contest, competitor);
    }
}
