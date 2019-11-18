package fxControllers;

import accessors.converters.ClubsConverter;
import accessors.converters.CompetitorsConverter;
import accessors.converters.TrainersConverter;
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
import model.Club;
import model.Competitor;
import model.Trainer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller class for {ClubView.fxml} file.
 * Supports operations for {@link Club} database table.
 * That class implements {@link Initializable} interface used with fxControllers in JavaFX.
 */
public class ClubController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // set clubTableView
        ObservableList<Club> clubList = FXCollections.observableArrayList();
        List<Club> cList = null;
        try {
            cList = clubsConverter.getAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        clubList.setAll(cList);
        clubTableView.setItems(clubList);
        setPropertiesForClubTableView();

        // setEditable clubTableView
        clubTableView.setEditable(true);
        // for name
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            Club club = event.getTableView().getItems().get(
                    event.getTablePosition().getRow()
            );
            if(!event.getNewValue().equals("")) {
                // update club's name
                club.setName(event.getNewValue());
                try {
                    clubsConverter.update(club);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                DialogsUtil.errorDialog("Pole nie może być puste!");
            }
            event.getTableView().refresh();
        });
        // for city
        cityColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        cityColumn.setOnEditCommit(event -> {
            Club club = event.getTableView().getItems().get(
                    event.getTablePosition().getRow()
            );
            if(!RegexUtil.cityRegex(event.getNewValue())) {
                DialogsUtil.errorDialog("Niepoprawna nazwa miasta!");
            } else if(!event.getNewValue().equals("")) {
                // update club's city
                club.setCity(event.getNewValue());
                try {
                    clubsConverter.update(club);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                DialogsUtil.errorDialog("Pole nie może być puste!");
            }
            event.getTableView().refresh();
        });

        // set trainerTableView
        setPropertiesForTrainerTableView();

        // set competitorTableView
        setPropertiesForCompetitorTableView();
    }

    /**
     * Called after pressing the {@link ClubController#addClubButton} and adding new {@link Club} to the database
     * using values selected in {@link ClubController#nameTextField} and {@link ClubController#cityTextField}.
     */
    @FXML public void addClub() throws IOException {

        // validate wrong data
        if(nameTextField.getText().isEmpty() || cityTextField.getText().isEmpty()) {
            DialogsUtil.errorDialog("Wypełniej wszystkie pola formularza, aby dodać nowy klub!");
            return;
        }

        // add club
        List<Club> clubBaseList = clubsConverter.getAll();
        Club club = new Club(
                0L,
                nameTextField.getText(),
                cityTextField.getText()
        );

        // if exists
        if(clubExists(clubBaseList, club)) {
            DialogsUtil.errorDialog("Taki klub już istnieje w bazie!");
            nameTextField.clear();
            cityTextField.clear();
            return;
        } else {
            // add
            clubsConverter.add(club);
        }

        // clearing
        nameTextField.clear();
        cityTextField.clear();

        // refresh view
        ObservableList<Club> clubList = FXCollections.observableArrayList();
        clubBaseList = clubsConverter.getAll();
        clubList.setAll(clubBaseList);
        clubTableView.setItems(clubList);
    }

    /**
     * Called after pressing {@link ClubController#removeClubButton} and removing selected one from the database,
     * using selected item in {@link ClubController#clubTableView}.
     */
    @FXML public void removeClub() throws IOException {

        // validate wrong data
        if(clubTableView.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Nie wybrano klubu do usunięcia!");
            return;
        } else if(clubHasTrainersOrCompetitors(clubTableView.getSelectionModel().getSelectedItem())) {
            DialogsUtil.errorDialog("Nie można usunąć klubu, do którego przypisani są zawodnicy lub trenerzy!");
            return;
        }

        // remove club
        Club club = clubTableView.getSelectionModel().getSelectedItem();
        clubsConverter.remove(club.getId());

        // clearing
        clubTableView.getSelectionModel().clearSelection();

        // refresh view
        ObservableList<Club> clubList = FXCollections.observableArrayList();
        List<Club> clubBaseList = clubsConverter.getAll();
        clubList.setAll(clubBaseList);
        clubTableView.setItems(clubList);
    }

    /**
     * Called after pressing {@link ClubController#clubTableView} and showing information about {@link Trainer}s
     * and {@link Competitor}s for selected {@link Club}.
     */
    @FXML public void setCompetitorsAndTrainersFromSelectedClub() throws IOException {

        Club club = clubTableView.getSelectionModel().getSelectedItem();

        // set trainerTableView
        ObservableList<Trainer> trainerList = FXCollections.observableArrayList();
        List<Trainer> trainerBaseList = (club != null) ? trainersConverter.getAllByClub(club.getId()) : new ArrayList<>();
        trainerList.setAll(trainerBaseList);
        trainerTableView.setItems(trainerList);

        // set competitorTableView
        ObservableList<Competitor> competitorList = FXCollections.observableArrayList();
        List<Competitor> competitorBaseList = (club != null) ? competitorsConverter.getAllByClub(club.getId()) : new ArrayList<>();
        competitorList.addAll(competitorBaseList);
        competitorTableView.setItems(competitorList);
    }

    /**
     * Sets properties for {@link ClubController#clubTableView}.
     */
    private void setPropertiesForClubTableView() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        clubTableView.setPlaceholder(new Label("Nie ma klubów do wyświetlenia"));
    }

    /**
     * Sets properties for {@link ClubController#trainerTableView}.
     */
    private void setPropertiesForTrainerTableView() {
        trainerTableView.setPlaceholder(new Label("Nie ma trenerów do wyświetlenia"));
        trainerColumn.setCellValueFactory(param -> new SimpleStringProperty(
                param.getValue().getName() + " " + param.getValue().getSurname()));
    }

    /**
     * Sets properties for {@link ClubController#competitorTableView}.
     */
    private void setPropertiesForCompetitorTableView() {
        competitorTableView.setPlaceholder(new Label("Nie ma zawodników do wyświetlenia"));
        competitorColumn.setCellValueFactory(param -> new SimpleStringProperty(
                param.getValue().getName() + " " + param.getValue().getSurname()));
    }

    /**
     * Checks if in the {@link List} exists {@link Club} with name and city like selected one.
     * @return true if exists, false otherwise.
     */
    private boolean clubExists(List<Club> clubBaseList, Club club) {
        for(Club c : clubBaseList) {
            if(c.getName().equals(club.getName())
                    && c.getCity().equals(club.getCity())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if selected {@link Club} has any {@link Trainer}s or {@link Competitor}s.
     * @return true if has any, false otherwise.
     */
    private boolean clubHasTrainersOrCompetitors(Club club) throws IOException {
        List<Trainer> trainers = trainersConverter.getAllByClub(club.getId());
        List<Competitor> competitors = competitorsConverter.getAllByClub(club.getId());
        return !trainers.isEmpty() || !competitors.isEmpty();
    }

    // --------------- VARIABLES --------------- //

    /**
     * {@link TextField} for club's name.
     */
    @FXML private TextField nameTextField;

    /**
     * {@link TextField} for club's city.
     */
    @FXML private TextField cityTextField;

    /**
     * {@link Button} to add a new {@link Club} object to the database.
     */
    @FXML private Button addClubButton;

    /**
     * {@link TableView} for {@link Club} class objects.
     */
    @FXML private TableView<Club> clubTableView;

    /**
     * {@link TableColumn} with club's name.
     */
    @FXML private TableColumn<Club, String> nameColumn;

    /**
     * {@link TableColumn} with club's city.
     */
    @FXML private TableColumn<Club, String> cityColumn;

    /**
     * {@link Button} to remove the selected {@link Club} object from the database.
     */
    @FXML private Button removeClubButton;

    /**
     * {@link TableView} for {@link Trainer} class objects.
     */
    @FXML private TableView<Trainer> trainerTableView;

    /**
     * {@link TableColumn} with {@link String} represents information about {@link Trainer} class object.
     */
    @FXML private TableColumn<Trainer, String> trainerColumn;

    /**
     * {@link TableView} for {@link Competitor} class objects.
     */
    @FXML private TableView<Competitor> competitorTableView;

    /**
     * {@link TableColumn} with {@link String} represents information about {@link Competitor} class object.
     */
    @FXML private TableColumn<Competitor, String> competitorColumn;

    private ClubsConverter clubsConverter = new ClubsConverter();
    private TrainersConverter trainersConverter = new TrainersConverter();
    private CompetitorsConverter competitorsConverter = new CompetitorsConverter();
}
