package fxControllers;

import accessors.converters.*;
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
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller class for {CompetitorView.fxml} file.
 * Supports operations for {@link Competitor} database table.
 * That class implements {@link Initializable} interface used with fxControllers in JavaFX.
 */
public class CompetitorController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // set clubComboBoxes
        ObservableList<Club> clubList = FXCollections.observableArrayList();
        List<Club> clubBaseList = null;
        try {
            clubBaseList = clubsConverter.getAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        clubList.setAll(clubBaseList);
        clubComboBox.setItems(clubList);
        clubComboBox2.setItems(clubList);

        // set competitorTableView
        ObservableList<Competitor> competitorList = FXCollections.observableArrayList();
        List<Competitor> competitorBaseList = null;
        try {
            competitorBaseList = competitorsConverter.getAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        competitorList.setAll(competitorBaseList);
        setPropertiesForCompetitorTableView();
        competitorTableView.setItems(competitorList);

        // setEditable competitorTableView
        competitorTableView.setEditable(true);
        // name
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            Competitor competitor = event.getTableView().getItems().get(
                    event.getTablePosition().getRow()
            );
            if (!RegexUtil.nameRegex(event.getNewValue())) {
                DialogsUtil.errorDialog("Podano niepoprawne imię!");
            } else if (!event.getNewValue().equals("")) {
                // update competitor's name
                competitor.setName(event.getNewValue());
                try {
                    competitorsConverter.update(competitor);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                DialogsUtil.errorDialog("Pole nie może być puste!");
            }
            event.getTableView().refresh();
        });
        // surname
        surnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        surnameColumn.setOnEditCommit(event -> {
            Competitor competitor = event.getTableView().getItems().get(
                    event.getTablePosition().getRow()
            );
            if (!RegexUtil.surnameRegex(event.getNewValue())) {
                DialogsUtil.errorDialog("Podano niepoprawne nazwisko!");
            } else if (!event.getNewValue().equals("")) {
                // update competitor's surname
                competitor.setSurname(event.getNewValue());
                try {
                    competitorsConverter.update(competitor);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                DialogsUtil.errorDialog("Pole nie może pozostać puste!");
            }
            event.getTableView().refresh();
        });

        // set competitionComboBox
        ObservableList<Competition> competitionList = FXCollections.observableArrayList();
        List<Competition> competitionBaseList = null;
        try {
            competitionBaseList = competitionsConverter.getAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        competitionList.setAll(competitionBaseList);
        competitionComboBox.setItems(competitionList);
        timeTextField.setPromptText("m m : s s : ms ms");

        // recordTableView
        setPropertiesForRecordTableView();
    }

    /**
     * Called after pressing the {@link CompetitorController#addCompetitorButton} and adding new {@link Competitor} to
     * the database using values selected in {@link CompetitorController#peselTextField},
     * {@link CompetitorController#nameTextField} and {@link CompetitorController#surnameTextField}.
     */
    @FXML public void addCompetitor() throws IOException {

        // validate wrong data
        if(peselTextField.getText().isEmpty() || nameTextField.getText().isEmpty() || surnameTextField.getText().isEmpty()
                || clubComboBox.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wypełnij wszystkie pola formularza, aby dodać nowego zawodnika!");
            return;
        } else if(!RegexUtil.peselRegex(peselTextField.getText())) {
            DialogsUtil.errorDialog("Numer pesel musi składać się z 11 cyfr");
            peselTextField.clear();
            return;
        } else if(!controlSum(peselTextField.getText())) {
            DialogsUtil.errorDialog("Podano niepoprawny numer pesel!");
            peselTextField.clear();
            return;
        } else if(peselExists(peselTextField.getText())) {
            DialogsUtil.errorDialog("Nie można dodać drugiego zawodnika o tym samym peselu!");
            return;
        } else if(!RegexUtil.nameRegex(nameTextField.getText())) {
            DialogsUtil.errorDialog("Podano niepoprawne imię!");
            nameTextField.clear();
            return;
        } else if(!RegexUtil.surnameRegex(surnameTextField.getText())) {
            DialogsUtil.errorDialog("Podano niepoprawne nazwisko!");
            surnameTextField.clear();
            return;
        }

        // add competitor
        Competitor competitor = new Competitor(
                Long.valueOf(peselTextField.getText()),
                nameTextField.getText(),
                surnameTextField.getText(),
                genderFromPesel(peselTextField.getText()),
                clubComboBox.getSelectionModel().getSelectedItem()
        );
        // add
        competitorsConverter.add(competitor);

        // clearing
        peselTextField.clear();
        nameTextField.clear();
        surnameTextField.clear();
        clubComboBox.getSelectionModel().clearSelection();

        // refresh view
        ObservableList<Competitor> competitorList = FXCollections.observableArrayList();
        List<Competitor> competitorBaseList = competitorsConverter.getAll();
        competitorList.setAll(competitorBaseList);
        competitorTableView.setItems(competitorList);
    }

    /**
     * Called after pressing {@link CompetitorController#removeCompetitorButton} and removing selected one from the
     * database using selected item in {@link CompetitorController#competitionComboBox}.
     */
    @FXML public void removeCompetitor() throws IOException {
        // validate wrong data
        if(clubComboBox2.getSelectionModel().isEmpty() || competitorComboBox.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz zawodnika do usunięcia!");
            return;
        } else if(competitorHasSomeRecord(competitorComboBox.getSelectionModel().getSelectedItem())) {
            DialogsUtil.errorDialog("Nie można usunąć zawodnika z przypisanymi rekordami!");
            return;
        } else if(competitorTakesPartInSomeContest(competitorComboBox.getSelectionModel().getSelectedItem())) {
            DialogsUtil.errorDialog("Nie można usunąć zawodnika zapisanego na zawody!");
        }
        Competitor competitor = competitorComboBox.getSelectionModel().getSelectedItem();
        competitorsConverter.remove(competitor.getPesel());

        // clearing
        competitorComboBox.getSelectionModel().clearSelection();
        competitorComboBox.setItems(null);

        // refresh view
        ObservableList<Competitor> competitorList = FXCollections.observableArrayList();
        List<Competitor> competitorBaseList = competitorsConverter.getAll();
        competitorList.setAll(competitorBaseList);
        competitorTableView.setItems(competitorList);
    }

    /**
     * Called after pressing {@link CompetitorController#removeeAllCompetitorsFromClubButton} and removing all {@link Competitor}s
     * from the database using selected item in {@link CompetitorController#clubComboBox2}.
     */
    @FXML public void removeAllCompetitorsFromClub() throws IOException {
        // TODO: GDZIES TUTAJ RZUCA NULLPOINTEREXCEPTION, ALE DZIALA
        // remove data from database
        if(clubComboBox2.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz klub, z którego chcesz usunąć zawodników!");
            return;
        } else if (anyCompetitorFromClubHasSomeRecord(clubComboBox2.getSelectionModel().getSelectedItem())) {
            DialogsUtil.errorDialog("Nie można usunąć zawodników, niektórzy posiadają rekordy, musisz je najpierw usunąć!");
            return;
        } else if(anyCompetitorFromClubTakesPartInSomeContest(clubComboBox2.getSelectionModel().getSelectedItem())) {
            DialogsUtil.errorDialog("Nie można usunąć zawodników, niektórzy z nich są zapisani na zawody, musisz ich najpierw wypisać!");
            return;
        }
        Club club = clubComboBox2.getSelectionModel().getSelectedItem();
        competitorsConverter.removeAllByClub(club.getId());

        //clearing
        clubComboBox2.getSelectionModel().clearSelection();
        competitorComboBox.setItems(null);
        // refresh data
        ObservableList<Competitor> competitorList = FXCollections.observableArrayList();
        List<Competitor> competitorBaseList = competitorsConverter.getAll();
        competitorList.setAll(competitorBaseList);
        competitorTableView.setItems(competitorList);
    }

    /**
     * Called after choosing item in {@link CompetitorController#clubComboBox2} and set all {@link Competitor}s
     * in {@link CompetitorController#competitorComboBox}.
     */
    @FXML public void setCompetitorsFromSelectedClub() throws IOException {
        Club club = clubComboBox2.getSelectionModel().getSelectedItem();
        ObservableList<Competitor> competitorList = FXCollections.observableArrayList();
        List<Competitor> competitorBaseList = (club != null) ? competitorsConverter.getAllByClub(club.getId()) : new ArrayList<>();
        competitorList.setAll(competitorBaseList);
        competitorComboBox.setItems(competitorList);
    }

    /**
     * Called after pressing the {@link CompetitorController#addRecordButton} and adding {@link Record} to the database
     * using values selected in {@link CompetitorController#timeTextField} and {@link CompetitorController#competitorTableView}.
     */
    @FXML public void addRecord() throws IOException {
        // validate wrong data
        if (competitorTableView.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Musisz wybrać zawodnika, któremu dodajesz rekord!");
            return;
        } else if (competitionComboBox.getSelectionModel().isEmpty() || timeTextField.getText().isEmpty()) {
            DialogsUtil.errorDialog("Musisz wypełnić wszystkie pola formularza, aby dodać rekord zawodnikowi!");
            return;
        } else if (!competitorTableView.getSelectionModel().getSelectedItem().getGender().
                equals(competitionComboBox.getSelectionModel().getSelectedItem().getGender())) {
            DialogsUtil.errorDialog("Nie możesz dodać zawodnikowi w konkurencji innej płci!");
            competitionComboBox.getSelectionModel().clearSelection();
            return;
        } else if(!RegexUtil.recordRegex(timeTextField.getText())) {
            DialogsUtil.errorDialog("Niepoprawny format czasu, wymagany: xx:yy:zz\n " +
                    "- dwie cyfry na minuty\n " +
                    "- dwie cyfy na sekundy\n " +
                    "- dwie cyfry na setne\n " +
                    "oddzielone dwukropkiem");
            timeTextField.clear();
            return;
        }

        // add record
        String[] time = timeTextField.getText().split(":");
        Record newRecord = new Record(
                0L,
                Integer.valueOf(time[0]),
                Integer.valueOf(time[1]),
                Integer.valueOf(time[2]),
                competitorTableView.getSelectionModel().getSelectedItem(),
                competitionComboBox.getSelectionModel().getSelectedItem()
        );

        Record newRecordAfterAdding =  recordsConverter.add(newRecord);
        if(newRecord.getMinutes().equals(newRecordAfterAdding.getMinutes()) &&
                newRecord.getSeconds().equals(newRecordAfterAdding.getSeconds()) &&
                newRecord.getHundredths().equals(newRecordAfterAdding.getHundredths())) {

        } else {
            DialogsUtil.errorDialog("Rekord jest wolniejszy od już istniejącego!");
        }

        // cleaning
        competitionComboBox.getSelectionModel().clearSelection();
        timeTextField.clear();

        // refresh view
        setRecordsForSelectedCompetitor();
    }

    /**
     * Called after pressing the {@link CompetitorController#removeRecordButton} and removing selected one from
     * database using selected items in {@link CompetitorController#competitorTableView} and
     * {@link CompetitorController#recordTableView}.
     */
    @FXML private void removeRecord() throws IOException {

        // validate wrong data
        if(competitorTableView.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz zawodnika, któremu chcesz usunąć wynik!");
            return;
        } else if(recordTableView.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz wynik do usunięcia!");
            return;
        }
        Record record = recordTableView.getSelectionModel().getSelectedItem();
        recordsConverter.remove(record.getId());

        // cleaning
        recordTableView.getSelectionModel().clearSelection();
        // refresh view
        competitorTableView.refresh();
        setRecordsForSelectedCompetitor();
    }

    /**
     * Called after select item in {@link CompetitorController#competitorTableView} and set {@link Record}s
     * in {@link CompetitorController#recordTableView}.
     */
    @FXML public void setRecordsForSelectedCompetitor() throws IOException {
        Competitor competitor = competitorTableView.getSelectionModel().getSelectedItem();
        ObservableList<Record> recordList = FXCollections.observableArrayList();
        List<Record> recordBaseList = (competitor != null) ? recordsConverter.getAllByCompetitor(competitor.getPesel()) : new ArrayList<>();
        recordList.setAll(recordBaseList);
        recordTableView.setItems(recordList);
    }

    /**
     * Sets properties for {@link CompetitorController#competitorTableView}.
     */
    private void setPropertiesForCompetitorTableView() {
        competitorTableView.setPlaceholder(new Label("Brak zawodników do wyświetlenia"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        clubColumn.setCellValueFactory(param -> new SimpleStringProperty(
                param.getValue().getClub().getName() + " " + param.getValue().getClub().getCity()));
    }

    /**
     * Sets properties for {@link CompetitorController#recordTableView}.
     */
    private void setPropertiesForRecordTableView() {
        recordTableView.setPlaceholder(new Label("Brak rekordów do wyświetlenia"));
        competitionColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getCompetition().toString()));
        timeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().toString()));
    }

    /**
     * Checks the control sum of personal ID is given.
     * @param pesel {@link String} value of personal ID.
     * @return true if control sum is correct, false in other case.
     */
    private boolean controlSum(String pesel) {
        int[] numbers = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
        int controlSum = 0;
        for(int i = 0; i < 10; i++) {
            controlSum += (numbers[i] * Character.getNumericValue(pesel.charAt(i)));
        }
        controlSum %= 10;
        controlSum = 10 - controlSum;
        controlSum %= 10;
        return Character.getNumericValue(pesel.charAt(10)) == controlSum;
    }

    /**
     * Select gender value using text with personal ID.
     * @param text {@link String} with personal ID.
     * @return "K" if it's woman or "M" if it's man.
     */
    private String genderFromPesel(String text) {
        if(Character.getNumericValue(text.charAt(9))%2 == 0) {
            return "K";
        } else return "M";
    }

    /**
     * Checks if {@link Competitor} with selected personal ID exists.
     * @return true if exists, false otherwise.
     */
    private boolean peselExists(String pesel) throws IOException {
        List<Competitor> competitorBaseList = competitorsConverter.getAll();
        for(Competitor competitor : competitorBaseList) {
            if(competitor.getPesel().equals(Long.valueOf(pesel))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if selected {@link Competitor} has some {@link Record}s.
     * @return true if has, false otherwise.
     */
    private boolean competitorHasSomeRecord(Competitor competitor) throws IOException {
        List<Record> recordBaseList = recordsConverter.getAllByCompetitor(competitor.getPesel());
        return !recordBaseList.isEmpty();
    }

    /**
     * Checks if any {@link Competitor} from selected {@link Club} has some {@link Record}s.
     * @return true if has, false otherwise.
     */
    private boolean anyCompetitorFromClubHasSomeRecord(Club club) throws IOException {
        List<Competitor> competitorFromClubBaseList = competitorsConverter.getAllByClub(club.getId());
        for(Competitor c : competitorFromClubBaseList) {
            if(competitorHasSomeRecord(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if selected {@link Competitor} takes part in some {@link Contest}s.
     * @return true if takes part, false otherwise.
     */
    private boolean competitorTakesPartInSomeContest(Competitor competitor) throws IOException {
        List<Contest> contestBaseList = contestsConverter.getAll();
        for(Contest c : contestBaseList) {
            if(c.getCompetitors().contains(competitor)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if any {@link Competitor} from selected {@link Club} takes part in some {@link Contest}.
     * @return true if takes part, false otherwise.
     */
    private boolean anyCompetitorFromClubTakesPartInSomeContest(Club club) throws IOException {
        List<Competitor> competitorFromClubBaseList = competitorsConverter.getAllByClub(club.getId());
        for(Competitor c : competitorFromClubBaseList) {
            if(competitorTakesPartInSomeContest(c)) {
                return true;
            }
        }
        return false;
    }

    // --------------- VARIABLES --------------- //

    /**
     * {@link TextField} for competitor's name.
     */
    @FXML private TextField nameTextField;

    /**
     * {@link TextField} for competitor's surname.
     */
    @FXML private TextField surnameTextField;

    /**
     * {@link TextField} for competitor's personal ID.
     */
    @FXML private TextField peselTextField;

    /**
     * {@link ComboBox} to select {@link Club} object.
     */
    @FXML private ComboBox<Club> clubComboBox;

    /**
     * {@link Button} to add a new {@link Competitor} object to the database.
     */
    @FXML private Button addCompetitorButton;

    /**
     * {@link ComboBox} to select {@link Club} object.
     */
    @FXML private ComboBox<Club> clubComboBox2;

    /**
     * {@link Button} to remove all {@link Competitor}s from selected {@link Club}.
     */
    @FXML private Button removeeAllCompetitorsFromClubButton;

    /**
     * {@link ComboBox} to select {@link Competitor} from selected {@link Club}.
     */
    @FXML private ComboBox<Competitor> competitorComboBox;

    /**
     * {@link Button} to remove selected {@link Competitor} from selected {@link Club}.
     */
    @FXML private Button removeCompetitorButton;

    /**
     * {@link TableView} for {@link Competitor} class objects.
     */
    @FXML private TableView<Competitor> competitorTableView;

    /**
     * {@link TableColumn} with competitor's name.
     */
    @FXML private TableColumn<Competitor, String> nameColumn;

    /**
     * {@link TableColumn} with competitor's surname.
     */
    @FXML private TableColumn<Competitor, String> surnameColumn;

    /**
     * {@link TableColumn} with competitor's gender.
     */
    @FXML private TableColumn<Competitor, String> genderColumn;

    /**
     * {@link TableColumn} with competitor's {@link Club}.
     */
    @FXML private TableColumn<Competitor, String> clubColumn;

    /**
     * {@link ComboBox} to select {@link Competition} object.
     */
    @FXML private ComboBox<Competition> competitionComboBox;

    /**
     * {@link TextField} for elements of {@link Record} class in format m m : s s : ms ms.
     */
    @FXML private TextField timeTextField;

    /**
     * {@link Button} to add new {@link Record} for selected {@link Competitor}.
     */
    @FXML private Button addRecordButton;

    /**
     * {@link Button} to remove selected {@link Record} object from the database.
     */
    @FXML private Button removeRecordButton;

    /**
     * {@link TableView} for informations about {@link Record} class objects.
     */
    @FXML private TableView<Record> recordTableView;

    /**
     * {@link TableColumn} with record's {@link Competition}.
     */
    @FXML private TableColumn<Record, String> competitionColumn;

    /**
     * {@link TableColumn} with record's value in format m m : s s : ms ms.
     */
    @FXML private TableColumn<Record, String> timeColumn;

    private CompetitorsConverter competitorsConverter = new CompetitorsConverter();
    private ClubsConverter clubsConverter = new ClubsConverter();
    private CompetitionsConverter competitionsConverter = new CompetitionsConverter();
    private RecordsConverter recordsConverter = new RecordsConverter();
    private ContestsConverter contestsConverter = new ContestsConverter();
}
