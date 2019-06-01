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
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompetitorController implements Initializable {

    @FXML private TextField nameTextField;

    @FXML private TextField surnameTextField;

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

        System.out.println(genderFromPesel("96040703840"));

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
            if(!event.getNewValue().equals("")) {
                HibernateUtil.getEm().getTransaction().begin();
                competitor.setName(event.getNewValue());
//            HibernateUtilCompetitor.updateCompetitor(competitor);
                HibernateUtil.getEm().getTransaction().commit();
            }
            event.getTableView().refresh();
        });
        surnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        surnameColumn.setOnEditCommit(event -> {
            Competitor competitor = event.getTableView().getItems().get(
                    event.getTablePosition().getRow()
            );
            if(!event.getNewValue().equals("")) {
                HibernateUtil.getEm().getTransaction().begin();
                competitor.setSurname(event.getNewValue());
                //HibernateUtilCompetitor.updateCompetitor(competitor);
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

    @FXML
    public void addCompetitor() {
        // add data to database
        if(peselTextField.getText().isEmpty() || nameTextField.getText().isEmpty() || surnameTextField.getText().isEmpty()
                || clubComboBox.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wypełnij wszystkie pola formularza, aby dodać nowego zawodnika!");
            return;
        } else if(!correctPeselRegex(peselTextField.getText())) {
            DialogsUtil.errorDialog("Numer pesel musi składać się z 11 cyfr");
            peselTextField.clear();
            return;
        } else if(HibernateUtil.getEm().find(Competitor.class, Long.valueOf(peselTextField.getText())) != null) {
            DialogsUtil.errorDialog("Zawodnik o takim peselu już istnieje!");
            peselTextField.clear();
            return;
        } else if(!controlSum(peselTextField.getText())) {
            DialogsUtil.errorDialog("Podano niepoprawny numer pesel!");
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

    private String genderFromPesel(String text) {
        if(Character.getNumericValue(text.charAt(9))%2 == 0) {
            return "K";
        } else return "M";
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
        } else if(!correctTimeRegex(timeTextField.getText())) {
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
        // TODO: POPRAWIC  TRIGGER - JESLI DODAJE REKORD GORSZY OD POPRZEDNIEGO TO SIE DODAJE A NIE POWINIEN
        if(!HibernateUtilRecord.addRecord(record)) {
            DialogsUtil.errorDialog("Nowy czas jest wolniejszy od już istniejącego!");
        }
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

//    private void setGenderComboBox() {
//        ObservableList<String> genderList = FXCollections.observableArrayList();
//        List gList = new ArrayList<String>() {
//            { add("M"); add("K"); }
//        };
//        genderList.addAll(gList);
//        genderComboBox.setItems(genderList);
//    }

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
        //genderComboBox.getSelectionModel().clearSelection();
        clubComboBox.getSelectionModel().clearSelection();
    }

    private boolean correctPeselRegex(String pesel) {
        Pattern pattern = Pattern.compile("[0-9]{11}");
        Matcher matcher = pattern.matcher(pesel);
        return matcher.matches();
    }

    private boolean correctTimeRegex(String time) {
        Pattern pattern = Pattern.compile("[0-9]{2}:[0-5][0-9]:[0-9]{2}");
        Matcher matcher = pattern.matcher(time);
        return matcher.matches();
    }

    private boolean controlSum(String text) {
        int[] numbers = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
        int controlSum = 0;
        for(int i = 0; i < 10; i++) {
            controlSum += (numbers[i] * Character.getNumericValue(text.charAt(i)));
        }
        controlSum %= 10;
        controlSum = 10 - controlSum;
        controlSum %= 10;
        return Character.getNumericValue(text.charAt(10)) == controlSum;
    }
}
