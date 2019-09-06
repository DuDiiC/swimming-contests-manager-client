package controllers;

import dbModels.Competition;
import dbModels.Competitor;
import dbModels.Contest;
import dbUtils.HibernateUtil;
import dbUtils.HibernateUtilContest;
import fxUtils.DialogsUtil;
import fxUtils.RegexUtil;
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

/**
 * Controller class for {ContestView.fxml} file.
 * Supports operations for {@link Contest} database table.
 * That class implements {@link Initializable} interface used with controllers in JavaFX.
 */
public class ContestController implements Initializable {

    /**
     * {@link TextField} for contest's name.
     */
    @FXML private TextField nameTextField;

    /**
     * {@link TextField} for contest's city.
     */
    @FXML private TextField cityTextField;

    /**
     * {@link DatePicker} for contest's start date.
     */
    @FXML private DatePicker dateField;

    /**
     * {@link Button} to add a new {@link Contest} object to the database.
     */
    @FXML private Button addContestButton;

    /**
     * {@link Button} to remove the selected {@link Contest} from the database.
     */
    @FXML private Button removeContestButton;

    /**
     * {@link TableView} for {@link Contest} class objects.
     */
    @FXML private TableView<Contest> contestTableView;

    /**
     * {@link TableColumn} with contest's name.
     */
    @FXML private TableColumn<Contest, String> nameColumn;

    /**
     * {@link TableColumn} with contest's city.
     */
    @FXML private TableColumn<Contest, String> cityColumn;

    /**
     * {@link TableColumn} with contest's start data information.
     */
    @FXML private TableColumn<Contest, String> dateColumn;

    /**
     * {@link TableView} for {@link Competitor}s in selected {@link Contest}.
     */
    @FXML private TableView<Competitor> competitorTableView;

    /**
     * {@link TableColumn} with competitor's information.
     */
    @FXML private TableColumn<Competitor, String> competitorColumn;

    /**
     * {@link Button} to remove selected {@link Competitor} from selected {@link Contest}.
     */
    @FXML private Button removeCompetitorFromContestButton;

    /**
     * {@link Button} to remove all {@link Competitor}s from selected {@link Contest}.
     */
    @FXML private Button removeAllCompetitorsFromContestButton;

    /**
     * {@link TableView} for {@link Competition}s in selected {@link Contest}.
     */
    @FXML private TableView<Competition> competitionTableView;

    /**
     * {@link TableColumn} with competition's information.
     */
    @FXML private TableColumn<Competition, String> competitionColumn;

    /**
     * {@link Button} to remove selected {@link Competition} from selected {@link Contest}.
     */
    @FXML private Button removeCompetitionFromContestButton;

    /**
     * {@link Button} to remove all {@link Competition}s from selected {@link Contest}.
     */
    @FXML private Button removeAllCompetitionsFromContestButton;

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
            if(!event.getNewValue().equals("")) {
                HibernateUtil.getEm().getTransaction().begin();
                contest.setName(event.getNewValue());
                HibernateUtil.getEm().getTransaction().commit();
            }
            event.getTableView().refresh();
        });
        cityColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        cityColumn.setOnEditCommit(event -> {
            Contest contest = event.getTableView().getItems().get(
                    event.getTablePosition().getRow()
            );
            if(!RegexUtil.cityRegex(event.getNewValue())) {
                DialogsUtil.errorDialog("Niepoprawna nazwa miasta!");
            } else if(!event.getNewValue().equals("")) {
                HibernateUtil.getEm().getTransaction().begin();
                contest.setCity(event.getNewValue());
                HibernateUtil.getEm().getTransaction().commit();
            }
            event.getTableView().refresh();
        });

        // competitorTableView
        competitorColumn.setCellValueFactory(param -> new SimpleStringProperty(
                param.getValue().toString() + " (" + param.getValue().getClub().toString() + ")"));
        competitorTableView.setPlaceholder(new Label("Nie ma zawodnikow do wyswietlenia"));

        // competitionTableView
        competitionColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().toString()));
        competitionTableView.setPlaceholder(new Label("Nie ma konkurencji do wyswietlenia"));
    }

    /**
     * Called after pressing the {@link ContestController#addContestButton} and adding new {@link Contest} to the database
     * using values selected in {@link ContestController#nameTextField}, {@link ContestController#cityTextField} and
     * {@link ContestController#dateField}.
     */
    @FXML private void addContest() {
        if(nameTextField.getText().isEmpty() || cityTextField.getText().isEmpty() || dateField.getEditor().getText().isEmpty()) {
            DialogsUtil.errorDialog("Wypełnij wszystkie pola formularza, aby dodać nowe zawody!");
            return;
        } else if(!RegexUtil.cityRegex(cityTextField.getText())) {
            DialogsUtil.errorDialog("Niepoprawna nazwa miasta!");
            cityTextField.clear();
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

    /**
     * Called after pressing {@link ContestController#removeContestButton} and removing selected one from the database
     * using selected item in {@link ContestController#contestTableView}.
     */
    @FXML private void removeContest() {
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

    /**
     * Called after pressing {@link ContestController#contestTableView} and showing information about {@link Competitor}s
     * and {@link Competition}s for selected {@link Contest}.
     */
    @FXML public void selectedInfo() {
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

    /**
     * Called after pressing {@link ContestController#removeCompetitorFromContestButton} and removing correlations
     * between selected {@link Contest} and selected {@link Competitor}.
     */
    @FXML public void removeCompetitorFromContest() {
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

    /**
     * Calling after pressing {@link ContestController#removeAllCompetitorsFromContestButton} and removing correlations
     * between selected {@link Contest} and all {@link Competitor}s.
     */
    @FXML private void removeAllCompetitorsFromContest() {
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

    /**
     * Called after pressing {@link ContestController#removeCompetitionFromContestButton} and removing correlations
     * between selected {@link Contest} and selected {@link Competition}.
     */
    @FXML private void removeCompetitionFromContest() {
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

    /**
     * Calling after pressing {@link ContestController#removeAllCompetitionsFromContestButton} and removing correlations
     * between selected {@link Contest} aand all {@link Competition}s.
     */
    @FXML private void removeAllCompetitionsFromContest() {
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
