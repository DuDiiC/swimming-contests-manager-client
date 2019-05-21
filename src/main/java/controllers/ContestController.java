package controllers;

import dbModels.Competition;
import dbModels.Competitor;
import dbModels.Contest;
import dbUtils.HibernateUtilContest;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ContestController implements Initializable {

    @FXML private TextField nameTextField;

    @FXML private TextField cityTextField;

    @FXML private DatePicker dateField;

    @FXML private Button addContestButton;

    @FXML private ComboBox<Contest> contestComboBox;

    @FXML private Button deleteContestButton;

    @FXML private TableView<Contest> contestTableView;

    @FXML private TableColumn<Contest, String> nameColumn;

    @FXML private TableColumn<Contest, String> cityColumn;

    @FXML private TableColumn<Contest, String> dateColumn;

    @FXML private TableView<Competitor> competitorTableView;

    @FXML private TableColumn<Competitor, String> competitorColumn;

    @FXML private TableView<Competition> competitionTableView;

    @FXML private TableColumn<Competition, String> competitionColumn;

    @FXML private Button deleteCompetitorFromContestButton;

    @FXML private Button deleteAllCompetitorsFromContestButton;

    @FXML private Button deleteCompetitionFromContestButton;

    @FXML private Button deleteAllCompetitionsFromContestButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<Contest> contestList = FXCollections.observableArrayList();
        contestList.setAll(HibernateUtilContest.getAll());

        // contestComboBox
        contestComboBox.setItems(contestList);

        // contestTableView
        nameColumn.setCellValueFactory(new PropertyValueFactory<Contest, String>("name"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<Contest, String>("city"));
        dateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Contest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Contest, String> param) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMMM yyyy");
                String stringDate = sdf.format(param.getValue().getDate());
                return new SimpleStringProperty(stringDate);
            }
        });
        contestTableView.setItems(contestList);

        // competitorTableView
        competitorColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Competitor, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Competitor, String> param) {
                return new SimpleStringProperty(param.getValue().toString() + " (" + param.getValue().getClub().toString() + ")");
            }
        });

        // competitionTableView
        competitionColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Competition, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Competition, String> param) {
                return new SimpleStringProperty(param.getValue().toString());
            }
        });
    }

    @FXML
    private void addContest() {
        // add data to database
        Contest contest = new Contest();
        contest.setName(nameTextField.getText());
        contest.setCity(cityTextField.getText());
        contest.setDate(Date.from(dateField.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        HibernateUtilContest.addContest(contest);
        // cleaning
        nameTextField.clear();
        cityTextField.clear();
        dateField.getEditor().clear();
        // refresh view
        ObservableList<Contest> contestList = FXCollections.observableArrayList();
        List cList = HibernateUtilContest.getAll();
        contestList.setAll(cList);
        contestComboBox.setItems(contestList);
        contestTableView.setItems(contestList);
    }

    @FXML
    private void removeContest() {
        // remove data from database
        Contest contest = contestComboBox.getSelectionModel().getSelectedItem();
        HibernateUtilContest.deleteContest(contest);
        // cleaning
        contestComboBox.getSelectionModel().clearSelection();
        // refresh view
        ObservableList<Contest> contestList = FXCollections.observableArrayList();
        List cList = HibernateUtilContest.getAll();
        contestList.setAll(cList);
        contestComboBox.setItems(contestList);
        contestTableView.setItems(contestList);
    }

    @FXML
    public void selectedInfo() {
        Contest contest = contestTableView.getSelectionModel().getSelectedItem();
        // competitorTableView
        ObservableList<Competitor> competitorList = FXCollections.observableArrayList();
        competitorList.addAll(HibernateUtilContest.getAllCompetitors(contest));
        competitorTableView.setItems(competitorList);
        // competitionTableView
        ObservableList<Competition> competitionList = FXCollections.observableArrayList();
        competitionList.addAll(HibernateUtilContest.getAllCompetitions(contest));
        competitionTableView.setItems(competitionList);
    }

    @FXML
    public void removeCompetitorFromContest() {
        Contest contest = contestTableView.getSelectionModel().getSelectedItem();
        Competitor competitor = competitorTableView.getSelectionModel().getSelectedItem();

        List<Competitor> cList = contest.getCompetitors();
        cList.remove(competitor);
        contest.setCompetitors(cList);

        HibernateUtilContest.addOrRemoveCompetitor(contest, competitor);

        selectedInfo();
    }

    @FXML
    private void removeAllCompetitorsFromContest() {
        Contest contest = contestTableView.getSelectionModel().getSelectedItem();

        List<Competitor> cList = new ArrayList<>();
        contest.setCompetitors(cList);

        HibernateUtilContest.addOrRemoveAllCompetitors(contest);

        selectedInfo();
    }

    @FXML
    private void removeCompetitionFromContest() {
        Contest contest = contestTableView.getSelectionModel().getSelectedItem();
        Competition competition = competitionTableView.getSelectionModel().getSelectedItem();

        List<Competition> cList = contest.getCompetitions();
        cList.remove(competition);
        contest.setCompetitions(cList);

        HibernateUtilContest.addOrRemoveCompetition(contest, competition);

        selectedInfo();
    }

    @FXML
    private void removeAllCompetitionsFromContest() {
        Contest contest = contestTableView.getSelectionModel().getSelectedItem();

        List<Competition> cList = new ArrayList<>();
        contest.setCompetitions(cList);

        HibernateUtilContest.addOrRemoveAllCompetitions(contest);

        selectedInfo();
    }
}
