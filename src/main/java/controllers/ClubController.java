package controllers;

import dbModels.Club;
import dbModels.Competitor;
import dbModels.Trainer;
import dbUtils.HibernateUtil;
import dbUtils.HibernateUtilClub;
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
 * Controller class for {ClubView.fxml} file.
 * Supports operations for {@link Club} database table.
 * That class implements {@link Initializable} interface used with controllers in JavaFX.
 */
public class ClubController implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<Club> clubList = getClub();
        List cList = HibernateUtilClub.getAll();
        clubList.setAll(cList);

        // clubTableView
        clubTableView.setItems(getClub());
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        clubTableView.setItems(clubList);
        clubTableView.setPlaceholder(new Label("Nie ma klubów do wyświetlenia"));

        // setEditable clubTableView
        clubTableView.setEditable(true);
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            Club club = event.getTableView().getItems().get(
                    event.getTablePosition().getRow()
            );
            if(!event.getNewValue().equals("")) {
                HibernateUtil.getEm().getTransaction().begin();
                club.setName(event.getNewValue());
                HibernateUtil.getEm().getTransaction().commit();
            }
            event.getTableView().refresh();
        });
        cityColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        cityColumn.setOnEditCommit(event -> {
            Club club = event.getTableView().getItems().get(
                    event.getTablePosition().getRow()
            );
            if(!RegexUtil.cityRegex(event.getNewValue())) {
                DialogsUtil.errorDialog("Niepoprawna nazwa miasta!");
            } else if(!event.getNewValue().equals("")) {
                HibernateUtil.getEm().getTransaction().begin();
                club.setCity(event.getNewValue());
                HibernateUtil.getEm().getTransaction().commit();
            }
            event.getTableView().refresh();
        });

        // trainerTableView
        trainerTableView.setPlaceholder(new Label("Nie ma trenerów do wyświetlenia"));
        trainerColumn.setCellValueFactory(param -> new SimpleStringProperty(
                param.getValue().getName() + " " + param.getValue().getSurname()));

        // competitorTableView
        competitorTableView.setPlaceholder(new Label("Nie ma zawodników do wyświetlenia"));
        competitorColumn.setCellValueFactory(param -> new SimpleStringProperty(
                param.getValue().getName() + " " + param.getValue().getSurname()));
    }

    /**
     * Called after pressing the {@link ClubController#addClubButton} and adding new {@link Club} to the database
     * using values selected in {@link ClubController#nameTextField} and {@link ClubController#cityTextField}.
     */
    @FXML
    public void addClub() {
        // add data to database
        if(nameTextField.getText().isEmpty() || cityTextField.getText().isEmpty()) {
            DialogsUtil.errorDialog("Wypełniej wszystkie pola formularza, aby dodać nowy klub!");
            return;
        }
        Club club = new Club();
        club.setName(nameTextField.getText());
        club.setCity(cityTextField.getText());
        if(!HibernateUtilClub.addClub(club)) {
            DialogsUtil.errorDialog("Taki klub już istnieje w bazie!");
            nameTextField.clear();
            cityTextField.clear();
            return;
        }
        // clearing
        nameTextField.clear();
        cityTextField.clear();
        // refresh view
        ObservableList<Club> clubList = FXCollections.observableArrayList();
        List cList = HibernateUtilClub.getAll();
        clubList.setAll(cList);
        clubTableView.setItems(clubList);
    }

    /**
     * Called after pressing {@link ClubController#removeClubButton} and removing selected one from the database,
     * using selected item in {@link ClubController#clubTableView}.
     */
    @FXML
    public void removeClub() {
        if(clubTableView.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Nie wybrano klubu do usunięcia!");
            return;
        } else if(clubTableView.getSelectionModel().getSelectedItem().getCompetitors().size() !=0
                || clubTableView.getSelectionModel().getSelectedItem().getTrainers().size() != 0) {
            DialogsUtil.errorDialog("Nie można usunąć klubu, do którego przypisani są zawodnicy lub trenerzy!");
            return;
        }
        // remove data from database
        Club club = clubTableView.getSelectionModel().getSelectedItem();
        HibernateUtilClub.removeClub(club);
        // clearing
        clubTableView.getSelectionModel().clearSelection();
        // refresh view
        ObservableList<Club> clubList = FXCollections.observableArrayList();
        List cList = HibernateUtilClub.getAll();
        clubList.setAll(cList);
        clubTableView.setItems(clubList);
    }

    /**
     * Called after pressing {@link ClubController#clubTableView} and showing information about {@link Trainer}s
     * and {@link Competitor}s for selected {@link Club}.
     */
    @FXML
    public void selectedInfo() {
        Club club = clubTableView.getSelectionModel().getSelectedItem();
        // trainerTableView
        ObservableList<Trainer> trainerList = FXCollections.observableArrayList();
        trainerList.addAll(HibernateUtilClub.getAllTrainers(club));
        trainerTableView.setItems(trainerList);
        // competitorTableView
        ObservableList<Competitor> competitorList = FXCollections.observableArrayList();
        competitorList.addAll(HibernateUtilClub.getAllCompetitors(club));
        competitorTableView.setItems(competitorList);
    }

    /**
     * Retrieves information about all {@link Club} class objects from the database.
     * @return {@link ObservableList} with all {@link Club}s from the database.
     */
    private ObservableList<Club> getClub() {
        ObservableList<Club> clubList = FXCollections.observableArrayList();
        clubList.addAll(HibernateUtilClub.getAll());
        return clubList;
    }
}
