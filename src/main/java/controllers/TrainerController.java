package controllers;

import dbModels.Club;
import dbModels.Trainer;
import dbUtils.HibernateUtilClub;
import dbUtils.HibernateUtilTrainer;
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

public class TrainerController implements Initializable {

    @FXML private TextField nameTextField;

    @FXML private TextField surnameTextField;

    @FXML private ComboBox<Club> clubComboBox;

    @FXML private Button addTrainerButton;

    @FXML private ComboBox<Club> clubComboBox2;

    @FXML private ComboBox<Trainer> trainerComboBox;

    @FXML private Button deleteTrainerButton;

    @FXML private TableView<Trainer> trainerTableView;

    @FXML private TableColumn<Trainer, Integer> licenceNrColumn;

    @FXML private TableColumn<Trainer, String> nameColumn;

    @FXML private TableColumn<Trainer, String> surnameColumn;

    @FXML private TableColumn<Trainer, String> clubColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // clubComboBox
        ObservableList<Club> clubList = FXCollections.observableArrayList();
        clubList.setAll(HibernateUtilClub.getAll());
        clubComboBox.setItems(clubList);
        clubComboBox2.setItems(clubList);

        // trainerTableView
        setPropertiesForTrainerTableView();
        trainerTableView.setItems(getTrainer());
    }

    @FXML
    public void addTrainer() {
        // add data to database
        Trainer trainer = new Trainer();
        trainer.setName(nameTextField.getText());
        trainer.setSurname(surnameTextField.getText());
        trainer.setClub(clubComboBox.getSelectionModel().getSelectedItem());
        HibernateUtilTrainer.addTrainer(trainer);
        // clearing
        nameTextField.clear();
        surnameTextField.clear();
        clubComboBox.getSelectionModel().clearSelection();
        // refresh view
        ObservableList<Trainer> trainerList = FXCollections.observableArrayList();
        List tList = HibernateUtilTrainer.getAll();
        trainerList.setAll(tList);
        trainerTableView.setItems(trainerList);
    }

    @FXML
    public void removeTrainer() {
        // remove data from database
        Trainer trainer = trainerComboBox.getSelectionModel().getSelectedItem();
        HibernateUtilTrainer.deleteTrainer(trainer);
        // clearing todo: this code throws an exception
        clubComboBox2.getSelectionModel().clearSelection();
        trainerComboBox.setItems(null);
        // refresh view
        ObservableList<Trainer> trainerList = FXCollections.observableArrayList();
        List tList = HibernateUtilTrainer.getAll();
        trainerList.setAll(tList);
        trainerTableView.setItems(trainerList);
    }

    @FXML
    public void setTrainers() {
        Club club = clubComboBox2.getSelectionModel().getSelectedItem();
        ObservableList<Trainer> trainerList = FXCollections.observableArrayList();
        List tList = HibernateUtilClub.getAllTrainers(club);
        trainerList.setAll(tList);
        trainerComboBox.setItems(trainerList);
    }

    private ObservableList<Trainer> getTrainer() {
        ObservableList<Trainer> trainerList = FXCollections.observableArrayList();
        trainerList.addAll(HibernateUtilTrainer.getAll());
        return trainerList;
    }

    private void setPropertiesForTrainerTableView() {
        licenceNrColumn.setCellValueFactory(new PropertyValueFactory<Trainer, Integer>("licenceNr"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Trainer, String>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<Trainer, String>("surname"));
        clubColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Trainer, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Trainer, String> param) {
                return new SimpleStringProperty(param.getValue().getClub().getName() + " " + param.getValue().getClub().getCity());
            }
        });
    }
}
