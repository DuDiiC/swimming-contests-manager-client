package controllers;

import dbModels.Club;
import dbModels.Competition;
import dbModels.Competitor;
import dbModels.Record;
import dbUtils.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CompetitorController implements Initializable {

    @FXML private TextField nameTextField;

    @FXML private TextField surnameTextField;

    @FXML private ComboBox<String> genderComboBox;

    @FXML private TextField peselTextField;

    @FXML private ComboBox<Club> clubComboBox;

    @FXML private Button addCompetitorButton;

    @FXML private ComboBox<Club> clubComboBox2;

    @FXML private Button deleteAllCompetitorsFromClubButton;

    @FXML private ComboBox<Competitor> competitorComboBox;

    @FXML private Button deleteCompetitorButton;

    @FXML private TableView<Competitor> competitorTableView;

    @FXML private TableColumn<Competitor, String> nameColumn;

    @FXML private TableColumn<Competitor, String> surnameColumn;

    @FXML private TableColumn<Competitor, String> genderColumn;

    @FXML private TableColumn<Competitor, String> clubColumn;

    @FXML private ComboBox<Competition> competitionComboBox;

    @FXML private TextField timeTextField;

    @FXML private Button addRecordButton;

    @FXML private Button deleteRecordButton;

    @FXML private TableView<Record> recordTableView;

    @FXML private TableColumn<Record, String> competitionColumn;

    @FXML private TableColumn<Record, String> timeColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // genderComboBox
        setGenderComboBox();

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
            competitor.setName(event.getNewValue());
            HibernateUtilCompetitor.updateCompetitor(competitor);
        });
        surnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        surnameColumn.setOnEditCommit(event -> {
            Competitor competitor = event.getTableView().getItems().get(
                    event.getTablePosition().getRow()
            );
            competitor.setSurname(event.getNewValue());
            HibernateUtilCompetitor.updateCompetitor(competitor);
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

    @FXML
    public void addCompetitor() {
        // add data to database
        if(peselTextField.getText().isEmpty() || nameTextField.getText().isEmpty() || surnameTextField.getText().isEmpty()
                || genderComboBox.getSelectionModel().isEmpty() || clubComboBox.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wypełnij wszystkie pola formularza, aby dodać nowego zawodnika!");
            return;
        } else if(false /* TODO: WALIDACJA NUMERU PESEL W FUNKCJI PO STRONIE SERWERA BAZY DANYCH */) {
            DialogsUtil.errorDialog("Podano niepoprawny numer pesel!");
            peselTextField.clear();
            return;
        }
        Competitor competitor = new Competitor();
        competitor.setPesel(Long.valueOf(peselTextField.getText()));
        competitor.setName(nameTextField.getText());
        competitor.setSurname(surnameTextField.getText());
        competitor.setGender(genderComboBox.getSelectionModel().getSelectedItem());
        competitor.setClub(clubComboBox.getSelectionModel().getSelectedItem());
        HibernateUtilCompetitor.addCompetitor(competitor);
        // clearing
        clearAfterAddition();
        // refresh view
        ObservableList<Competitor> competitorList = getCompetitors();
        competitorTableView.setItems(competitorList);
        competitorComboBox.setItems(competitorList);
    }

    @FXML
    public void removeCompetitor() {
        // remove data from database
        if(clubComboBox2.getSelectionModel().isEmpty() || competitorComboBox.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz zawodnika do usunięcia!");
            return;
        }
        Competitor competitor = competitorComboBox.getSelectionModel().getSelectedItem();
        HibernateUtilCompetitor.removeCompetitor(competitor);
        // clearing
        competitorComboBox.getSelectionModel().clearSelection();
        // refresh view
        ObservableList<Competitor> competitorList = getCompetitors();
        competitorTableView.setItems(competitorList);
        competitorComboBox.setItems(competitorList);
    }

    @FXML
    public void removeAllCompetitorsFromClub() {
        // remove data from database
        if(clubComboBox2.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz klub, z którego chcesz usunąć zawodników!");
            return;
        }
        Club club = clubComboBox2.getSelectionModel().getSelectedItem();
        HibernateUtilCompetitor.removeAllCompetitorsFromClub(club);
        //clearing
        competitorComboBox.setItems(null);
        clubComboBox2.getSelectionModel().clearSelection(); // todo: czemu rzuca wyjatek?
        // refresh data
        ObservableList<Competitor> competitorList = getCompetitors();
        competitorTableView.setItems(competitorList);
    }

    @FXML
    public void setCompetitors() {
        Club club = clubComboBox2.getSelectionModel().getSelectedItem();
        ObservableList<Competitor> competitorList = FXCollections.observableArrayList();
        List cList = HibernateUtilClub.getAllCompetitors(club);
        competitorList.setAll(cList);
        competitorComboBox.setItems(competitorList);
    }

    @FXML
    public void addRecord() {
        // add data to database
        if(competitorTableView.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Musisz wybrać zawodnika, któremu dodajesz rekord!");
            return;
        } else if(competitionComboBox.getSelectionModel().isEmpty() || timeTextField.getText().isEmpty()) {
            DialogsUtil.errorDialog("Musisz wypełnić wszystkie pola formularza, aby dodać rekord zawodnikowi!");
            return;
        } else if(false /* TODO: DOPISAC REGEX NA SPRAWDZANIE POPRAWNOSCI FORMATU CZASU */) {
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
        // TODO: NAPISAC DODAWANIE REKORDU TAK, ABY DZIALAL TRIGGER, AKTUALNIE PO PROSTU DODAJE KOLEJNE REKORDY
        HibernateUtilRecord.addRecord(record);
        // cleaning
        competitionComboBox.getSelectionModel().clearSelection();
        timeTextField.clear();
        // refresh view
        selectedInfo();
    }

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
        selectedInfo();
    }

    @FXML
    public void selectedInfo() {
        Competitor competitor = competitorTableView.getSelectionModel().getSelectedItem();
        ObservableList<Record> recordList = getRecordsFromCompetitor(competitor);
        recordTableView.setItems(recordList);
    }

    private ObservableList<Competitor> getCompetitors() {
        ObservableList<Competitor> competitorList = FXCollections.observableArrayList();
        competitorList.addAll(HibernateUtilCompetitor.getAll());
        return competitorList;
    }

    private ObservableList<Record> getRecordsFromCompetitor(Competitor competitor) {
        ObservableList<Record> recordList = FXCollections.observableArrayList();
        recordList.addAll(HibernateUtilCompetitor.getAllRecords(competitor));
        return recordList;
    }

    private void setGenderComboBox() {
        ObservableList<String> genderList = FXCollections.observableArrayList();
        List gList = new ArrayList<String>() {
            { add("M"); add("K"); }
        };
        genderList.addAll(gList);
        genderComboBox.setItems(genderList);
    }

    private void setPropertiesForCompetitorTableView() {
        competitorTableView.setPlaceholder(new Label("Brak zawodników do wyświetlenia"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        clubColumn.setCellValueFactory(param -> new SimpleStringProperty(
                param.getValue().getClub().getName() + " " + param.getValue().getClub().getCity()));
    }

    private void clearAfterAddition() {
        peselTextField.clear();
        nameTextField.clear();
        surnameTextField.clear();
        genderComboBox.getSelectionModel().clearSelection();
        clubComboBox.getSelectionModel().clearSelection();
    }
}
