package controllers;

import dbModels.Club;
import dbModels.Competitor;
import dbModels.Trainer;
import dbUtils.HibernateUtilClub;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ClubController implements Initializable {

    @FXML private TextField nameTextField;

    @FXML private TextField cityTextField;

    @FXML private Button addClubButton;

    @FXML private ComboBox<Club> clubComboBox;

    @FXML private Button deleteClubButton;

    @FXML private TableView<Club> clubTableView;

    @FXML private TableColumn<Club, String> nameColumn;

    @FXML private TableColumn<Club, String> cityColumn;

    @FXML private TableView<Trainer> trainerTableView;

    @FXML private TableColumn<Trainer, String> trainerColumn;

    @FXML private TableView<Competitor> competitorTableView;

    @FXML private TableColumn<Competitor, String> competitorColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<Club> clubList = getClub();
        List cList = HibernateUtilClub.getAll();
        // clubs comboBox
        clubList.setAll(cList);
        clubComboBox.setItems(clubList);

        // clubs tableView
        clubTableView.setItems(getClub());
        nameColumn.setCellValueFactory(new PropertyValueFactory<Club, String>("name"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<Club, String>("city"));
        clubTableView.setItems(clubList);

        // trainerTableView
        trainerColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Trainer, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Trainer, String> param) {
                return new SimpleStringProperty(param.getValue().getName() + " " + param.getValue().getSurname());
            }
        });

        // competitorTableView
        competitorColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Competitor, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Competitor, String> param) {
                return new SimpleStringProperty(param.getValue().getName() + " " + param.getValue().getSurname());
            }
        });
    }

    @FXML
    public void addClub() {
        // add data to database
        Club club = new Club();
        club.setName(nameTextField.getText());
        club.setCity(cityTextField.getText());
        HibernateUtilClub.addClub(club);
        // clearing
        nameTextField.clear();
        cityTextField.clear();
        // refresh view
        ObservableList<Club> clubList = FXCollections.observableArrayList();
        List cList = HibernateUtilClub.getAll();
        clubList.setAll(cList);
        clubTableView.setItems(clubList);
        clubComboBox.setItems(clubList);
    }

    @FXML
    public void removeClub() {
        // remove data from database
        Club club = clubComboBox.getSelectionModel().getSelectedItem();
        HibernateUtilClub.deleteClub(club);
        // clearing
        clubComboBox.getSelectionModel().clearSelection();
        // refresh view
        ObservableList<Club> clubList = FXCollections.observableArrayList();
        List cList = HibernateUtilClub.getAll();
        clubList.setAll(cList);
        clubTableView.setItems(clubList);
        clubComboBox.setItems(clubList);
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
