package fxControllers;

import accessors.converters.CompetitionsConverter;
import accessors.converters.ContestsConverter;
import accessors.converters.RecordsConverter;
import fxUtils.DialogsUtil;
import fxUtils.RegexUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Competition;
import model.Contest;
import model.Record;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller class for {CompetitionView.fxml} file.
 * Supports operations for {@link Competition} database table.
 * That class implements {@link Initializable} interface used with fxControllers in JavaFX.
 */
public class CompetitionController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // set styleChoiceBox
        ObservableList<String> styleList = FXCollections.observableArrayList();
        List<String> sList = new ArrayList<String>() {
            { add("dowolny"); add("grzbietowy"); add("klasyczny"); add("motylkowy"); add("zmienny"); }
        };
        styleList.addAll(sList);
        styleComboBox.setItems(styleList);
//
        // set genderChoiceBox
        ObservableList<String> genderList = FXCollections.observableArrayList();
        List gList = new ArrayList<String>() {
            { add("M"); add("K"); }
        };
        genderList.addAll(gList);
        genderComboBox.setItems(genderList);

        // set competitionTableView
        setPropertiesForCompetitionTableView();
        ObservableList<Competition> competitionList = FXCollections.observableArrayList();
        List<Competition> competitionBaseList = null;
        try {
            competitionBaseList = competitionsConverter.getAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        competitionList.setAll(competitionBaseList);
        competitionTableView.setItems(competitionList);

        // set contestComboBox
        ObservableList<Contest> contestList = FXCollections.observableArrayList();
        try {
            contestList.setAll(contestsConverter.getAll());
        } catch (IOException e) {
            e.printStackTrace();
        }
        contestComboBox.setItems(contestList);
    }

    /**
     * Called after pressing the {@link CompetitionController#addCompetitionButton} and adding {@link Competition} to the
     * database using values selected in {@link CompetitionController#styleComboBox}, {@link CompetitionController#distanceTextField}
     * and {@link CompetitionController#genderComboBox}.
     */
    @FXML public void addCompetition() throws IOException {

        // validate wrong data
        if(styleComboBox.getSelectionModel().isEmpty() || distanceTextField.getText().isEmpty() || genderComboBox.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wypełnij wszystkie pola formularza, aby dodać nową konkurencję!");
            return;
        } else if(!RegexUtil.distanceRegex(distanceTextField.getText())) {
            DialogsUtil.errorDialog("Niepoprawny dystans w konkurencji - długość niecki basenu to 25 m," +
                    " musisz podać wielokrotność tej liczby.");
            distanceTextField.clear();
            return;
        }

        // add competition
        List<Competition> competitionBaseList = competitionsConverter.getAll();
        Competition competition = new Competition(
                0L,
                styleComboBox.getSelectionModel().getSelectedItem(),
                Integer.valueOf(distanceTextField.getText()),
                genderComboBox.getSelectionModel().getSelectedItem()
        );

        // if exists
        if(competitionExists(competitionBaseList, competition)) {
            DialogsUtil.errorDialog("Taka konkurencja już istnieje!");
            styleComboBox.getSelectionModel().clearSelection();
            distanceTextField.clear();
            genderComboBox.getSelectionModel().clearSelection();
            return;
        } else {
            // add
            competitionsConverter.add(competition);
        }

        // clearing
        styleComboBox.getSelectionModel().clearSelection();
        distanceTextField.clear();
        genderComboBox.getSelectionModel().clearSelection();

        // refresh view
        ObservableList<Competition> competitionList = FXCollections.observableArrayList();
        competitionBaseList = competitionsConverter.getAll();
        competitionList.setAll(competitionBaseList);
        competitionTableView.setItems(competitionList);
    }

    /**
     * Called after pressing {@link CompetitionController#removeCompetitionButton} and removing selected one from the
     * database using selected item in {@link CompetitionController#competitionTableView}.
     */
    @FXML public void removeCompetition() throws IOException {
        if (competitionTableView.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz konkurencję do usunięcia!");
            return;
        } else if(competitionHasRecords(competitionTableView.getSelectionModel().getSelectedItem())) {
            DialogsUtil.errorDialog("Nie można usunąć konkurencji z przypisanymi rekordami!");
            return;
        } else if(competitionBelongsToSomeContest(competitionTableView.getSelectionModel().getSelectedItem())) {
            DialogsUtil.errorDialog("Nie można usunąć konkurencji przypisanej do zawodów!");
            return;
        }

        // remove competition
        Competition competition = competitionTableView.getSelectionModel().getSelectedItem();
        competitionsConverter.remove(competition.getId());

        // clearing
        competitionTableView.getSelectionModel().clearSelection();

        // refresh view
        ObservableList<Competition> competitionList = FXCollections.observableArrayList();
        List<Competition> competitionBaseList = competitionsConverter.getAll();
        competitionList.setAll(competitionBaseList);
        competitionTableView.setItems(competitionList);
    }

    /**
     * Called after pressing {@link CompetitionController#addCompetitionToContestButton} and adding relation
     * between selected {@link Competition} and {@link Contest}.
     */
    @FXML public void addCompetitionToContest() {

        // TODO: DOPISAC PO NAPRAWIENIU PROBLEMU Z WYSWIETLANIEM ZAWODOW
        // validate wrong data
        if(competitionTableView.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz konkurecję do dopisania do zawodów!");
            return;
        } else if(contestComboBox.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz zawody, do których chces zapisać konkurecję!");
            return;
        }

        // add competition to contest
//        Contest contest = contestComboBox.getSelectionModel().getSelectedItem();
//        Competition competition = competitionTableView.getSelectionModel().getSelectedItem();
//        for(Competition c : contest.getCompetitions()) {
//            if(c.getCompetitionId() == competition.getCompetitionId()) {
//                DialogsUtil.errorDialog("Ta konkurencja jest już przypisana do wybranych zawodów!");
//                return;
//            }
//        }
//        List<Competition> cL = contest.getCompetitions();
//        cL.add(competition);
//        contest.setCompetitions(cL);
//
//        HibernateUtilContest.addOrRemoveCompetition(contest);
//
//        // clearing
//        competitionTableView.getSelectionModel().clearSelection();
//        contestComboBox.getSelectionModel().clearSelection();
    }

    /**
     * Sets properties for {@link CompetitionController#competitionTableView}.
     */
    private void setPropertiesForCompetitionTableView() {
        competitionTableView.setPlaceholder(new Label("Nie ma konkurencji do wyświetlenia"));
        styleColumn.setCellValueFactory(new PropertyValueFactory<>("style"));
        distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        recordColumn.setCellValueFactory(param -> {
            try {
                Record record = recordsConverter.getBestByCompetition(param.getValue().getId());
                if(record.toString().equals("")) {
                    return new SimpleStringProperty("brak rekordu");
                }
                return new SimpleStringProperty(record.toString() + " (" + record.getCompetitor().toString()
                        + " - " + record.getCompetitor().getClub().toString() +")");
            } catch (NullPointerException e) {
                return null;
            } catch (IOException e) {
                return null;
            }
        });
    }

    /**
     * Checks if in the {@link List} exists {@link Competition} with distance, style and gender like selected one.
     * @return true if exists, false otherwise.
     */
    private boolean competitionExists(List<Competition> competitionBaseList, Competition competition) {
        for(Competition c : competitionBaseList) {
            if(c.getStyle().equals(competition.getStyle())
                    && c.getGender().equals(competition.getGender())
                    && c.getDistance().equals(competition.getDistance())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if selected {@link Competition} has any {@link Record}s.
     * @return true if has, false otherwise.
     */
    private boolean competitionHasRecords(Competition competition) throws IOException {
        List<Record> recordBaseList = recordsConverter.getAll();
        for(Record r : recordBaseList) {
            if(r.getCompetition().getId().equals(competition.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if selected {@link Competition} belongs to sone {@link Contest}.
     * @return true if belongs, false otherwise.
     */
    private boolean competitionBelongsToSomeContest(Competition competition) throws IOException {
        List<Contest> contestBaseList = contestsConverter.getAll();
        for(Contest c : contestBaseList) {
            if(c.getCompetitions().contains(competition)) {
                return true;
            }
        }
        return false;
    }

    // --------------- VARIABLES --------------- //

    /**
     * {@link ComboBox} for competition's style.
     */
    @FXML private ComboBox<String> styleComboBox;

    /**
     * {@link TextField} for competition's distance.
     */
    @FXML private TextField distanceTextField;

    /**
     * {@link ChoiceBox} for competition's gender.
     */
    @FXML private ChoiceBox<String> genderComboBox;

    /**
     * {@link Button} to add a new {@link Competition} object to the database.
     */
    @FXML private Button addCompetitionButton;

    /**
     * {@link TableView} for {@link Competition} class objects.
     */
    @FXML private TableView<Competition> competitionTableView;

    /**
     * {@link TableColumn} with competition's style.
     */
    @FXML private TableColumn<Competition, String> styleColumn;

    /**
     * {@link TableColumn} with competition's distance.
     */
    @FXML private TableColumn<Competition, Integer> distanceColumn;

    /**
     * {@link TableColumn} with competition's gender.
     */
    @FXML private TableColumn<Competition, String> genderColumn;

    /**
     * {@link TableColumn} with competition's record in format m m : s s : ms ms.
     */
    @FXML private TableColumn<Competition, String> recordColumn;

    /**
     * {@link Button} to remove the selected {@link Competition} object from the database.
     */
    @FXML private Button removeCompetitionButton;

    /**
     * {@link ComboBox} to select {@link Contest} object.
     */
    @FXML private ComboBox<Contest> contestComboBox;

    /**
     * {@link Button} to add a relation between selected {@link Competition} and {@link Contest}.
     */
    @FXML private Button addCompetitionToContestButton;

    private CompetitionsConverter competitionsConverter = new CompetitionsConverter();
    private ContestsConverter contestsConverter = new ContestsConverter();
    private RecordsConverter recordsConverter = new RecordsConverter();
}
