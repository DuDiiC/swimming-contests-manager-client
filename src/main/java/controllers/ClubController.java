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

public class ClubController implements Initializable {

    @FXML private TextField nameTextField;

    @FXML private TextField cityTextField;

    @FXML private Button addClubButton;

    @FXML private TableView<Club> clubTableView;

    @FXML private TableColumn<Club, String> nameColumn;

    @FXML private TableColumn<Club, String> cityColumn;

    @FXML private Button deleteClubButton;

    @FXML private TableView<Trainer> trainerTableView;

    @FXML private TableColumn<Trainer, String> trainerColumn;

    @FXML private TableView<Competitor> competitorTableView;

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

    private ObservableList<Club> getClub() {
        ObservableList<Club> clubList = FXCollections.observableArrayList();
        clubList.addAll(HibernateUtilClub.getAll());
        return clubList;
    }
}
