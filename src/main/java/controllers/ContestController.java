package controllers;

import dbModels.Competition;
import dbModels.Competitor;
import dbModels.Contest;
import dbUtils.HibernateUtilContest;
import fxUtils.DialogsUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * TODO:
 * > dodac modyfikacje elementow w tabeli do: nazwy, miasta, daty
 */
public class ContestController implements Initializable {

    @FXML private TextField nameTextField;

    @FXML private TextField cityTextField;

    @FXML private DatePicker dateField;

    @FXML private Button addContestButton;

    @FXML private Button deleteContestButton;

    @FXML private TableView<Contest> contestTableView;

    @FXML private TableColumn<Contest, String> nameColumn;

    @FXML private TableColumn<Contest, String> cityColumn;

    @FXML private TableColumn<Contest, String> dateColumn;

    @FXML private TableView<Competitor> competitorTableView;

    @FXML private TableColumn<Competitor, String> competitorColumn;

    @FXML private Button deleteCompetitorFromContestButton;

    @FXML private Button deleteAllCompetitorsFromContestButton;

    @FXML private TableView<Competition> competitionTableView;

    @FXML private TableColumn<Competition, String> competitionColumn;

    @FXML private Button deleteCompetitionFromContestButton;

    @FXML private Button deleteAllCompetitionsFromContestButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<Contest> contestList = FXCollections.observableArrayList();
        contestList.setAll(HibernateUtilContest.getAll());

        // contestTableView
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        dateColumn.setCellValueFactory(param -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMMM yyyy");
            String stringDate = sdf.format(param.getValue().getDate());
            return new SimpleStringProperty(stringDate);
        });
        contestTableView.setPlaceholder(new Label("Nie ma zawodow do wyswietlenia"));
        contestTableView.setItems(contestList);

        // setEditable contestTableView
        contestTableView.setEditable(true);
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            Contest contest = event.getTableView().getItems().get(
                    event.getTablePosition().getRow()
            );
            contest.setName(event.getNewValue());
            HibernateUtilContest.updateContest(contest);
        });
        cityColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        cityColumn.setOnEditCommit(event -> {
            Contest contest = event.getTableView().getItems().get(
                    event.getTablePosition().getRow()
            );
            contest.setCity(event.getNewValue());
            HibernateUtilContest.updateContest(contest);
        });

        // competitorTableView
        competitorColumn.setCellValueFactory(param -> new SimpleStringProperty(
                param.getValue().toString() + " (" + param.getValue().getClub().toString() + ")"));
        competitorTableView.setPlaceholder(new Label("Nie ma zawodnikow do wyswietlenia"));

        // competitionTableView
        competitionColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().toString()));
        competitionTableView.setPlaceholder(new Label("Nie ma konkurencji do wyswietlenia"));
    }

    @FXML
    private void addContest() {
        if(nameTextField.getText().isEmpty() || cityTextField.getText().isEmpty() || dateField.getEditor().getText().isEmpty()) {
            DialogsUtil.errorDialog("Wypełnij wszystkie pola formularza, aby dodać nowe zawody!");
            return;
        }
        // add data to database
        Contest contest = new Contest();
        contest.setName(nameTextField.getText());
        contest.setCity(cityTextField.getText());
        contest.setDate(Date.from(dateField.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        if(!HibernateUtilContest.addContest(contest)) {
            DialogsUtil.errorDialog("Takie zawody juz istnieja!");
            nameTextField.clear();
            cityTextField.clear();
            dateField.getEditor().clear();
            return;
        }
        // cleaning
        nameTextField.clear();
        cityTextField.clear();
        dateField.getEditor().clear();
        // refresh view
        ObservableList<Contest> contestList = FXCollections.observableArrayList();
        List cList = HibernateUtilContest.getAll();
        contestList.setAll(cList);
        contestTableView.setItems(contestList);
    }

    @FXML
    private void removeContest() {
        // remove data from database
        if(contestTableView.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz zawody do usunięcia!");
            return;
        }
        Contest contest = contestTableView.getSelectionModel().getSelectedItem();
        HibernateUtilContest.removeContest(contest);
        // cleaning
        competitorTableView.setItems(null);
        competitionTableView.setItems(null);
        // refresh view
        ObservableList<Contest> contestList = FXCollections.observableArrayList();
        List cList = HibernateUtilContest.getAll();
        contestList.setAll(cList);
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
        if(contestTableView.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz zawody, z których chcesz usunąć zawodnika!");
            return;
        } else if(competitorTableView.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz zawodnika do usunięcia!");
            return;
        }
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
        if(contestTableView.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz zawody, z których chcesz usunąć zawodników!");
            return;
        }
        Contest contest = contestTableView.getSelectionModel().getSelectedItem();

        List<Competitor> cList = new ArrayList<>();
        contest.setCompetitors(cList);

        HibernateUtilContest.addOrRemoveAllCompetitors(contest);

        selectedInfo();
    }

    @FXML
    private void removeCompetitionFromContest() {
        if(contestTableView.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz zawody, z których chcesz usunąć konkurencję!");
            return;
        } else if(competitionTableView.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz konkurencję, którą chcesz usunąć z zawodów!");
            return;
        }
        Contest contest = contestTableView.getSelectionModel().getSelectedItem();
        Competition competition = competitionTableView.getSelectionModel().getSelectedItem();

        List<Competition> cList = contest.getCompetitions();
        cList.remove(competition);
        contest.setCompetitions(cList);

        HibernateUtilContest.addOrRemoveCompetition(contest);

        selectedInfo();
    }

    @FXML
    private void removeAllCompetitionsFromContest() {
        if(contestTableView.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz zawody, z których chcesz usunąć konkurencje!");
            return;
        }
        Contest contest = contestTableView.getSelectionModel().getSelectedItem();

        List<Competition> cList = new ArrayList<>();
        contest.setCompetitions(cList);

        HibernateUtilContest.addOrRemoveAllCompetitions(contest);

        selectedInfo();
    }
}
