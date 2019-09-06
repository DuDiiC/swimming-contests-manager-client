package controllers;

import dbModels.*;
import dbUtils.*;
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
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller class for {CompetitorView.fxml} file.
 * Supports operations for {@link Competitor} database table.
 * That class implements {@link Initializable} interface used with controllers in JavaFX.
 */
public class CompetitorController implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        System.out.println(genderFromPesel("96040703840"));

        // genderComboBox
        //setGenderComboBox();

        // clubComboBox
        ObservableList<Club> clubList = FXCollections.observableArrayList();
        clubList.addAll(HibernateUtilClub.getAll());
        clubComboBox.setItems(clubList);

        // clubComboBox2
        clubComboBox2.setItems(clubList);

        // competitorList
        ObservableList<Competitor> competitorList = FXCollections.observableArrayList();
        competitorList.addAll(HibernateUtilCompetitor.getAll());

        // competitorTableView
        setPropertiesForCompetitorTableView();
        competitorTableView.setItems(competitorList);

        // setEditable competitorTableView
        competitorTableView.setEditable(true);
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            Competitor competitor = event.getTableView().getItems().get(
                event.getTablePosition().getRow()
            );
            if(!RegexUtil.nameRegex(event.getNewValue())) {
                DialogsUtil.errorDialog("Podano niepoprawne imię!");
            } else if(!event.getNewValue().equals("")) {
                HibernateUtil.getEm().getTransaction().begin();
                competitor.setName(event.getNewValue());
                HibernateUtil.getEm().getTransaction().commit();
            }
            event.getTableView().refresh();
        });
        surnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        surnameColumn.setOnEditCommit(event -> {
            Competitor competitor = event.getTableView().getItems().get(
                    event.getTablePosition().getRow()
            );
            if(!RegexUtil.surnameRegex(event.getNewValue())) {
                DialogsUtil.errorDialog("Podano niepoprawne nazwisko!");
            } else if(!event.getNewValue().equals("")) {
                HibernateUtil.getEm().getTransaction().begin();
                competitor.setSurname(event.getNewValue());
                HibernateUtil.getEm().getTransaction().commit();
            }
            event.getTableView().refresh();
        });

        // competitionComboBox
        ObservableList<Competition> competitionList = FXCollections.observableArrayList();
        List<Competition> cList = HibernateUtilCompetition.getAll();
        competitionList.setAll(cList);
        competitionComboBox.setItems(competitionList);
        timeTextField.setPromptText("format: mm:ss:ss");

        // recordTableView
        recordTableView.setPlaceholder(new Label("Brak rekordów do wyświetlenia"));
        competitionColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getCompetition().toString()));
        timeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().toString()));
    }

    /**
     * Called after pressing the {@link CompetitorController#addCompetitorButton} and adding new {@link Competitor} to
     * the database using values selected in {@link CompetitorController#peselTextField},
     * {@link CompetitorController#nameTextField} and {@link CompetitorController#surnameTextField}.
     */
    @FXML
    public void addCompetitor() {
        // add data to database
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
        } else if(!RegexUtil.nameRegex(nameTextField.getText())) {
            DialogsUtil.errorDialog("Podano niepoprawne imię!");
            nameTextField.clear();
            return;
        } else if(!RegexUtil.surnameRegex(surnameTextField.getText())) {
            DialogsUtil.errorDialog("Podano niepoprawne nazwisko!");
            surnameTextField.clear();
            return;
        } else if(HibernateUtil.getEm().find(Competitor.class, Long.valueOf(peselTextField.getText())) != null) {
            DialogsUtil.errorDialog("Zawodnik o takim peselu już istnieje!");
            peselTextField.clear();
            return;
        }
        Competitor competitor = new Competitor();
        competitor.setPesel(Long.valueOf(peselTextField.getText()));
        competitor.setName(nameTextField.getText());
        competitor.setSurname(surnameTextField.getText());
        competitor.setGender(genderFromPesel(peselTextField.getText()));
        competitor.setClub(clubComboBox.getSelectionModel().getSelectedItem());
        HibernateUtilCompetitor.addCompetitor(competitor);
        // clearing
        clearAfterAddition();
        // refresh view
        ObservableList<Competitor> competitorList = getCompetitors();
        competitorTableView.setItems(competitorList);
        competitorComboBox.setItems(competitorList);
    }

    /**
     * Called after pressing {@link CompetitorController#removeCompetitorButton} and removing selected one from the
     * database using selected item in {@link CompetitorController#competitionComboBox}.
     */
    @FXML
    public void removeCompetitor() {
        // remove data from database
        if(clubComboBox2.getSelectionModel().isEmpty() || competitorComboBox.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz zawodnika do usunięcia!");
            return;
        } else if(competitorComboBox.getSelectionModel().getSelectedItem().getRecords().size() != 0) {
            DialogsUtil.errorDialog("Nie można usunąć zawodnika z przypisanymi rekordami!");
            return;
        } else if(competitorComboBox.getSelectionModel().getSelectedItem().getContests().size() != 0) {
            DialogsUtil.errorDialog("Nie można usunąć zawodnika zapisanego na zawody!");
        }
        Competitor competitor = competitorComboBox.getSelectionModel().getSelectedItem();
        HibernateUtilCompetitor.removeCompetitor(competitor);
        // clearing
        competitorComboBox.getSelectionModel().clearSelection();
        // refresh view
        ObservableList<Competitor> competitorList = getCompetitors();
        competitorTableView.setItems(competitorList);
        competitorComboBox.setItems(null);
    }

    /**
     * Called after pressing {@link CompetitorController#removeeAllCompetitorsFromClubButton} and removing all {@link Competitor}s
     * from the database using selected item in {@link CompetitorController#clubComboBox2}.
     */
    @FXML
    public void removeAllCompetitorsFromClub() {
        // remove data from database
        if(clubComboBox2.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz klub, z którego chcesz usunąć zawodników!");
            return;
        } else if(someHaveRecords(clubComboBox2.getSelectionModel().getSelectedItem())) {
            DialogsUtil.errorDialog("Nie można usunąć zawodników, niektórzy posiadają rekordy, musisz je najpierw usunąć!");
            return;
        } else if (someTakePartInContests(clubComboBox2.getSelectionModel().getSelectedItem())) {
            DialogsUtil.errorDialog("Nie można usunąć zawodników, niektórzy z nich są zapisani na zawody, musisz ich najpierw wypisać!");
            return;
        }
        Club club = clubComboBox2.getSelectionModel().getSelectedItem();
        HibernateUtilCompetitor.removeAllCompetitorsFromClub(club);
        //clearing
        competitorComboBox.setItems(null);
        clubComboBox2.getSelectionModel().clearSelection();
        // refresh data
        ObservableList<Competitor> competitorList = getCompetitors();
        competitorTableView.setItems(competitorList);
    }

    /**
     * Called after choosing item in {@link CompetitorController#clubComboBox2} and set all {@link Competitor}s
     * in {@link CompetitorController#competitorComboBox}.
     */
    @FXML
    public void setCompetitors() {
        Club club = clubComboBox2.getSelectionModel().getSelectedItem();
        ObservableList<Competitor> competitorList = FXCollections.observableArrayList();
        List cList = HibernateUtilClub.getAllCompetitors(club);
        competitorList.setAll(cList);
        competitorComboBox.setItems(competitorList);
    }

    /**
     * Called after pressing the {@link CompetitorController#addRecordButton} and adding {@link Record} to the database
     * using values selected in {@link CompetitorController#timeTextField} and {@link CompetitorController#competitorTableView}.
     */
    @FXML
    public void addRecord() {
        // add data to database
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
        Record record = new Record();
        String[] time = timeTextField.getText().split(":");
        record.setMinutes(Integer.valueOf(time[0]));
        record.setSeconds(Integer.valueOf(time[1]));
        record.setHundredth(Integer.valueOf(time[2]));
        record.setCompetitor(competitorTableView.getSelectionModel().getSelectedItem());
        record.setCompetition(competitionComboBox.getSelectionModel().getSelectedItem());

        // dodanie do bazy rekordu
        if(!HibernateUtilRecord.addRecord(record)) {
            DialogsUtil.errorDialog("Nowy czas jest wolniejszy od już istniejącego!");
        }
        // cleaning
        competitionComboBox.getSelectionModel().clearSelection();
        timeTextField.clear();
        // refresh view
        selectedInfo();
    }

    /**
     * Called after pressing the {@link CompetitorController#removeRecordButton} and removing selected one from
     * database using selected items in {@link CompetitorController#competitorTableView} and
     * {@link CompetitorController#recordTableView}.
     */
    @FXML
    private void removeRecord() {
        if(competitorTableView.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz zawodnika, któremu chcesz usunąć wynik!");
            return;
        } else if(recordTableView.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz wynik do usunięcia!");
            return;
        }
        Record record = recordTableView.getSelectionModel().getSelectedItem();
        HibernateUtilRecord.removeRecord(record);
        // cleaning
        recordTableView.getSelectionModel().clearSelection();
        // refresh view
        competitorTableView.refresh();
        selectedInfo();
    }

    /**
     * Called after select item in {@link CompetitorController#competitorTableView} and set {@link Record}s
     * in {@link CompetitorController#recordTableView}.
     */
    @FXML
    public void selectedInfo() {
        Competitor competitor = competitorTableView.getSelectionModel().getSelectedItem();
        ObservableList<Record> recordList = getRecordsFromCompetitor(competitor);
        recordTableView.setItems(recordList);
    }

    /**
     * Retrieves information about all {@link Competitor} class objects from the database.
     * @return {@link ObservableList} with all {@link Competitor}s from the database.
     */
    private ObservableList<Competitor> getCompetitors() {
        ObservableList<Competitor> competitorList = FXCollections.observableArrayList();
        competitorList.addAll(HibernateUtilCompetitor.getAll());
        return competitorList;
    }

    /**
     * Retrieves information about all {@link Record}s of given {@link Competitor}.
     * @param competitor {@link Competition} for which the {@link Record}s are given.
     * @return {@link ObservableList} with all {@link Record}s of given {@link Competitor}.
     */
    private ObservableList<Record> getRecordsFromCompetitor(Competitor competitor) {
        ObservableList<Record> recordList = FXCollections.observableArrayList();
        recordList.addAll(HibernateUtilCompetitor.getAllRecords(competitor));
        return recordList;
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
     * Clears all JavaFX elements after adding new {@link Competitor}.
     */
    private void clearAfterAddition() {
        peselTextField.clear();
        nameTextField.clear();
        surnameTextField.clear();
        //genderComboBox.getSelectionModel().clearSelection();
        clubComboBox.getSelectionModel().clearSelection();
    }

    /**
     * Checks the control sum of personal ID is given.
     * @param pesel {@link String} value of personal ID.
     * @return true if control sum is correct, false in other case.
     */
    private boolean controlSum(String pesel) {
//        int[] numbers = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
//        int controlSum = 0;
//        for(int i = 0; i < 10; i++) {
//            controlSum += (numbers[i] * Character.getNumericValue(text.charAt(i)));
//        }
//        controlSum %= 10;
//        controlSum = 10 - controlSum;
//        controlSum %= 10;
//        return Character.getNumericValue(text.charAt(10)) == controlSum;
        String result = HibernateUtil.getEm().createNativeQuery("SELECT pesel_control_sum(:pesel) FROM dual")
                .setParameter("pesel", Long.valueOf(pesel)).getSingleResult().toString();
        return result.equals("T");
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
     * Checks if any {@link Competitor} has any {@link Record}.
     * @param club {@link Club} from which {@link Competitor}s are checked.
     * @return true if any of {@link Competitor}s has a {@link Record}, false in other case.
     */
    private boolean someHaveRecords(Club club) {
        for(Competitor comp : club.getCompetitors()) {
            if(comp.getRecords().size() != 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if any {@link Competitor} takes part in any {@link Contest}.
     * @param club {@link Club} from which {@link Competitor}s are checked.
     * @return true if any of {@link Competitor}s takes part in any {@link Contest}, false in other case.
     */
    private boolean someTakePartInContests(Club club) {
        for(Competitor comp : club.getCompetitors()) {
            if(comp.getContests().size() != 0) {
                return true;
            }
        }
        return false;
    }
}
