package controllers;

import dbModels.Competition;
import dbModels.Contest;
import dbModels.Record;
import dbUtils.HibernateUtilCompetition;
import dbUtils.HibernateUtilContest;
import fxUtils.DialogsUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;

public class CompetitionController implements Initializable {

    @FXML private ComboBox<String> styleComboBox;

    @FXML private TextField distanceTextField;

    @FXML private ChoiceBox<String> genderComboBox;

    @FXML private Button addCompetitionButton;

    @FXML private TableView<Competition> competitionTableView;

    @FXML private TableColumn<Competition, String> styleColumn;

    @FXML private TableColumn<Competition, Integer> distanceColumn;

    @FXML private TableColumn<Competition, String> genderColumn;

    @FXML private TableColumn<Competition, String> recordColumn;

    @FXML private Button deleteCompetitionButton;

    @FXML private ComboBox<Contest> contestComboBox;

    @FXML private Button addCompetitionToContestButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // styleChoiceBox
        ObservableList<String> styleList = FXCollections.observableArrayList();
        List sList = new ArrayList<String>() {
            { add("dowolny"); add("grzbietowy"); add("klasyczny"); add("motylkowy"); add("zmienny"); }
        };
        styleList.addAll(sList);
        styleComboBox.setItems(styleList);

        // genderChoiceBox
        ObservableList<String> genderList = FXCollections.observableArrayList();
        List gList = new ArrayList<String>() {
            { add("M"); add("K"); }
        };
        genderList.addAll(gList);
        genderComboBox.setItems(genderList);

        // competitionTableView
        competitionTableView.setPlaceholder(new Label("Nie ma konkurencji do wyświetlenia"));
        styleColumn.setCellValueFactory(new PropertyValueFactory<>("style"));
        distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        recordColumn.setCellValueFactory(param -> {
            try {
                Record record = getBestRecord(param.getValue());
                if(record == null) {
                    return new SimpleStringProperty("brak rekordu");
                }
                return new SimpleStringProperty(record.toString() + " (" + record.getCompetitor().toString()
                        + " - " + record.getCompetitor().getClub().toString() +")");
            } catch (NullPointerException e) {
                return null;
            }
        });
        competitionTableView.setItems(getCompetition());

        // contestComboBox
        ObservableList<Contest> contestList = FXCollections.observableArrayList();
        contestList.setAll(HibernateUtilContest.getAll());
        contestComboBox.setItems(contestList);
    }

    @FXML
    public void addCompetition() {
        if(styleComboBox.getSelectionModel().isEmpty() || distanceTextField.getText().isEmpty() || genderComboBox.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wypełnij wszystkie pola formularza, aby dodać nową konkurencję!");
            return;
        } else if(Integer.valueOf(distanceTextField.getText())%25 != 0) {
            DialogsUtil.errorDialog("Niepoprawny dystans w konkurencji - długość niecki basenu to 25 m," +
                    " musisz podać wielokrotność tej liczby.");
            distanceTextField.clear();
            return;
        }
        // add data to database
        Competition competition = new Competition();
        competition.setStyle(styleComboBox.getSelectionModel().getSelectedItem());
        competition.setDistance(Integer.valueOf(distanceTextField.getText()));
        competition.setGender(genderComboBox.getSelectionModel().getSelectedItem());
        HibernateUtilCompetition.addCompetition(competition);
        // clearing
        styleComboBox.getSelectionModel().clearSelection();
        distanceTextField.clear();
        genderComboBox.getSelectionModel().clearSelection();
        // refresh view
        competitionTableView.setItems(getCompetition());
    }

    @FXML
    public void removeCompetition() {
        if(competitionTableView.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz konkurencję do usunięcia!");
            return;
        } else if(competitionTableView.getSelectionModel().getSelectedItem().getRecords().size() != 0) {
            DialogsUtil.errorDialog("Nie można usunąć konkurencji z przypisanymi rekordami!");
            return;
        } else if(competitionTableView.getSelectionModel().getSelectedItem().getContests().size() != 0) {
            DialogsUtil.errorDialog("Nie można usunąć konkurencji przypisanej do zawodów!");
            return;
        }
        // remove data from database
        HibernateUtilCompetition.removeCompetition(competitionTableView.getSelectionModel().getSelectedItem());
        // clearing
        competitionTableView.getSelectionModel().clearSelection();
        // refresh view
        competitionTableView.setItems(getCompetition());
    }

    @FXML
    public void addCompetitionToContest() {
        // add data to database
        if(competitionTableView.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz konkurecję do dopisania do zawodów!");
            return;
        } else if(contestComboBox.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz zawody, do których chces zapisać konkurecję!");
            return;
        }
        Contest contest = contestComboBox.getSelectionModel().getSelectedItem();
        Competition competition = competitionTableView.getSelectionModel().getSelectedItem();
        for(Competition c : contest.getCompetitions()) {
            if(c.getCompetitionId() == competition.getCompetitionId()) {
                DialogsUtil.errorDialog("Ta konkurencja jest już przypisana do wybranych zawodów!");
                return;
            }
        }
        List<Competition> cL = contest.getCompetitions();
        cL.add(competition);
        contest.setCompetitions(cL);

        HibernateUtilContest.addOrRemoveCompetition(contest);

        // clearing
        competitionTableView.getSelectionModel().clearSelection();
        contestComboBox.getSelectionModel().clearSelection();
    }

    private ObservableList<Competition> getCompetition() {
        ObservableList<Competition> competitionList = FXCollections.observableArrayList();
        competitionList.addAll(HibernateUtilCompetition.getAll());
        return competitionList;
    }

    private static Record getBestRecord(Competition competition) {
        List<Record> recordList = HibernateUtilCompetition.getAllRecords(competition);
        if(recordList.size() == 0) {
            return null;
        }
        Record record = recordList.get(0);
        for(Record r : recordList) {
            if(record.getMinutes()*60 + record.getSeconds() > r.getMinutes()*60 + r.getSeconds()) {
                record = r;
            } else if((record.getMinutes()*60 + record.getSeconds() == r.getMinutes()*60 + r.getSeconds())
                    && record.getHundredth() > r.getHundredth()) {
                record = r;
            }
        }
        return record;
    }
}
