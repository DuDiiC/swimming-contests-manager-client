package fxControllers;

import accessors.converters.ContestsConverter;
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
import model.Competition;
import model.Competitor;
import model.Contest;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Controller class for {ContestView.fxml} file.
 * Supports operations for {@link Contest} database table.
 * That class implements {@link Initializable} interface used with fxControllers in JavaFX.
 */
public class ContestController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // set contestTableView
        ObservableList<Contest> contestList = FXCollections.observableArrayList();
        List<Contest> contestBaseList = null;
        try {
            contestBaseList = contestsConverter.getAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        contestList.setAll(contestBaseList);

        // contestTableView
        setPropertiesForContestTableView();
        contestTableView.setItems(contestList);

        // setEditable contestTableView
        contestTableView.setEditable(true);
        // name
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            Contest contest = event.getTableView().getItems().get(
                    event.getTablePosition().getRow()
            );
            if(!event.getNewValue().equals("")) {
                // update contest's name
                contest.setName(event.getNewValue());
                try {
                    contestsConverter.update(contest);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                DialogsUtil.errorDialog("Pole nie może zostać puste!");
            }
            event.getTableView().refresh();
        });
        // city
        cityColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        cityColumn.setOnEditCommit(event -> {
            Contest contest = event.getTableView().getItems().get(
                    event.getTablePosition().getRow()
            );
            if(!RegexUtil.cityRegex(event.getNewValue())) {
                DialogsUtil.errorDialog("Niepoprawna nazwa miasta!");
            } else if(!event.getNewValue().equals("")) {
                // update contest's city
                contest.setCity(event.getNewValue());
                try {
                    contestsConverter.update(contest);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            event.getTableView().refresh();
        });

        // set competitorTableView
        setPropertiesForCompetitorTableView();

        // set competitionTableView
        setPropertiesForCompetitionTableView();
    }

    /**
     * Called after pressing the {@link ContestController#addContestButton} and adding new {@link Contest} to the database
     * using values selected in {@link ContestController#nameTextField}, {@link ContestController#cityTextField} and
     * {@link ContestController#dateField}.
     */
    @FXML private void addContest() throws IOException {

        // validate wrong data
        if(nameTextField.getText().isEmpty() || cityTextField.getText().isEmpty() || dateField.getEditor().getText().isEmpty()) {
            DialogsUtil.errorDialog("Wypełnij wszystkie pola formularza, aby dodać nowe zawody!");
            return;
        } else if(!RegexUtil.cityRegex(cityTextField.getText())) {
            DialogsUtil.errorDialog("Niepoprawna nazwa miasta!");
            cityTextField.clear();
            return;
        }

        // add contest
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        List<Contest> contestBaseList = contestsConverter.getAll();
        Contest contest = new Contest(
                0L,
                nameTextField.getText(),
                LocalDate.parse(dateField.getEditor().getText(), formatter),
                cityTextField.getText(),
                new HashSet<Competition>(),
                new HashSet<Competitor>()
        );

        // if exists
        if(contestExists(contest)) {
            DialogsUtil.errorDialog("Takie zawody już istnieją w bazie!");
            nameTextField.clear();
            cityTextField.clear();
            dateField.getEditor().clear();
            return;
        } else {
            contestsConverter.add(contest);
        }

        // cleaning
        nameTextField.clear();
        cityTextField.clear();
        dateField.getEditor().clear();

        // refresh view
        ObservableList<Contest> contestList = FXCollections.observableArrayList();
        contestBaseList = contestsConverter.getAll();
        contestList.setAll(contestBaseList);
        contestTableView.setItems(contestList);
    }

    /**
     * Called after pressing {@link ContestController#removeContestButton} and removing selected one from the database
     * using selected item in {@link ContestController#contestTableView}.
     */
    @FXML private void removeContest() throws IOException {

        // validate wrong data
        if(contestTableView.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz zawody do usunięcia!");
            return;
        }

        // remove contest
        Contest contest = contestTableView.getSelectionModel().getSelectedItem();
        if(!contest.getCompetitors().isEmpty() || !contest.getCompetitions().isEmpty()) {
            DialogsUtil.errorDialog("Nie można usunąć zawodów z zapisami zawodnikami ani dodanymi konkurencjami!");
            return;
        }
        // TODO: SPRAWDZIC CZEMU RZUCA BLEDEM PRZY USUWANIU MIMO ZE USUWA???
        contestsConverter.remove(contest.getId());

        // cleaning
        competitorTableView.setItems(null);
        competitionTableView.setItems(null);

        // refresh view
        ObservableList<Contest> contestList = FXCollections.observableArrayList();
        List<Contest> contestBaseList = contestsConverter.getAll();
        contestList.setAll(contestBaseList);
        contestTableView.setItems(contestList);
    }

    /**
     * Called after pressing {@link ContestController#contestTableView} and showing information about {@link Competitor}s
     * and {@link Competition}s for selected {@link Contest}.
     */
    @FXML public void setCompetitionsAndCompetitorsForSelectedContest() {
        // TODO: NIE DZIALA
        Contest contest = contestTableView.getSelectionModel().getSelectedItem();
        // set competitorTableView
        ObservableList<Competitor> competitorList = FXCollections.observableArrayList();
        competitorList.addAll(contest.getCompetitors());
        competitorTableView.setItems(competitorList);
        // set competitionTableView
        ObservableList<Competition> competitionList = FXCollections.observableArrayList();
        competitionList.addAll(contest.getCompetitions());
        competitionTableView.setItems(competitionList);
    }

    /**
     * Called after pressing {@link ContestController#removeCompetitorFromContestButton} and removing correlations
     * between selected {@link Contest} and selected {@link Competitor}.
     */
    @FXML public void removeCompetitorFromContest() throws IOException {

        // validate wrong data
        if(contestTableView.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz zawody, z których chcesz usunąć zawodnika!");
            return;
        } else if(competitorTableView.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz zawodnika do usunięcia!");
            return;
        }

        // remove competitor from contest
        Contest selectedContest = contestTableView.getSelectionModel().getSelectedItem();
        Competitor selectedCompetitor = competitorTableView.getSelectionModel().getSelectedItem();

        Set<Competitor> competitorsFromContest = selectedContest.getCompetitors();
        competitorsFromContest.remove(selectedCompetitor);

        selectedContest.setCompetitors(competitorsFromContest);

        // update
        contestsConverter.update(selectedContest);

        // refresh
        setCompetitionsAndCompetitorsForSelectedContest();
    }

    /**
     * Calling after pressing {@link ContestController#removeAllCompetitorsFromContestButton} and removing correlations
     * between selected {@link Contest} and all {@link Competitor}s.
     */
    @FXML private void removeAllCompetitorsFromContest() throws IOException {

        // validate wrong data
        if(contestTableView.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz zawody, z których chcesz usunąć zawodników!");
            return;
        }
        // remove all competitors from contest
        Contest selectedContest = contestTableView.getSelectionModel().getSelectedItem();
        selectedContest.setCompetitors(new HashSet<>());

        // update
        contestsConverter.update(selectedContest);

        // refresh
        setCompetitionsAndCompetitorsForSelectedContest();
    }

    /**
     * Called after pressing {@link ContestController#removeCompetitionFromContestButton} and removing correlations
     * between selected {@link Contest} and selected {@link Competition}.
     */
    @FXML private void removeCompetitionFromContest() throws IOException {

        // validate wrong data
        if(contestTableView.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz zawody, z których chcesz usunąć konkurencję!");
            return;
        } else if(competitionTableView.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz konkurencję, którą chcesz usunąć z zawodów!");
            return;
        }

        // remove competition from contest
        Contest selectedContest = contestTableView.getSelectionModel().getSelectedItem();
        Competition selectedCompetition = competitionTableView.getSelectionModel().getSelectedItem();

        Set<Competition> competitionsFromContest = selectedContest.getCompetitions();
        competitionsFromContest.remove(selectedCompetition);

        selectedContest.setCompetitions(competitionsFromContest);

        // update
        contestsConverter.update(selectedContest);

        // refresh
        setCompetitionsAndCompetitorsForSelectedContest();
    }

    /**
     * Calling after pressing {@link ContestController#removeAllCompetitionsFromContestButton} and removing correlations
     * between selected {@link Contest} aand all {@link Competition}s.
     */
    @FXML private void removeAllCompetitionsFromContest() throws IOException {

        // validate wrong data
        if(contestTableView.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz zawody, z których chcesz usunąć konkurencje!");
            return;
        }

        // remove all competitions from contest
        Contest selectedContest = contestTableView.getSelectionModel().getSelectedItem();
        selectedContest.setCompetitions(new HashSet<>());

        // update
        contestsConverter.update(selectedContest);

        // refresh
        setCompetitionsAndCompetitorsForSelectedContest();
    }

    /**
     * Sets properties for {@link ContestController#contestTableView}.
     */
    private void setPropertiesForContestTableView() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        contestTableView.setPlaceholder(new Label("Nie ma zawodow do wyswietlenia"));
    }

    /**
     * Sets properties for {@link ContestController#competitionTableView}.
     */
    private void setPropertiesForCompetitionTableView() {
        competitionColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().toString()));
        competitionTableView.setPlaceholder(new Label("Nie ma konkurencji do wyswietlenia"));
    }

    /**
     * Sets properties for {@link ContestController#competitorTableView}.
     */
    private void setPropertiesForCompetitorTableView() {
        competitorColumn.setCellValueFactory(param -> new SimpleStringProperty(
                param.getValue().toString() + " (" + param.getValue().getClub().toString() + ")"));
        competitorTableView.setPlaceholder(new Label("Nie ma zawodnikow do wyswietlenia"));
    }

    /**
     * Checks if in the {@link List} exists {@link Contest} with name, city and date like selected one.
     * @return true if exists, false otherwise.
     */
    private boolean contestExists(Contest contest) throws IOException {
        List<Contest> contestBaseList = contestsConverter.getAll();
        for(Contest c : contestBaseList) {
            if(c.getName().equals(contest.getName())
                    && c.getCity().equals(contest.getCity())
                    && c.getDate().isEqual(contest.getDate())) {
                return true;
            }
        }
        return false;
    }

    // --------------- VARIABLES --------------- //

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

    private ContestsConverter contestsConverter = new ContestsConverter();

}
