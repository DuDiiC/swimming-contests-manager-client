package controllers;

import dbModels.Club;
import dbModels.Trainer;
import dbUtils.HibernateUtilClub;
import dbUtils.HibernateUtilTrainer;
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

        // setEditable trainerTableView
        trainerTableView.setEditable(true);
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            Trainer trainer = event.getTableView().getItems().get(
                    event.getTablePosition().getRow()
            );
            trainer.setName(event.getNewValue());
            HibernateUtilTrainer.updateTrainer(trainer);
        });
        surnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        surnameColumn.setOnEditCommit(event -> {
            Trainer trainer = event.getTableView().getItems().get(
                    event.getTablePosition().getRow()
            );
            trainer.setSurname(event.getNewValue());
            HibernateUtilTrainer.updateTrainer(trainer);
        });
    }

    @FXML
    public void addTrainer() {
        // add data to database
        if(nameTextField.getText().isEmpty() || surnameTextField.getText().isEmpty() || clubComboBox.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wypełnij wszystkie pola formularza, aby dodać nowego trenera!");
            return;
        }
        Trainer trainer = new Trainer();
        trainer.setName(nameTextField.getText());
        trainer.setSurname(surnameTextField.getText());
        trainer.setClub(clubComboBox.getSelectionModel().getSelectedItem());
        if(!HibernateUtilTrainer.addTrainer(trainer)) {
            DialogsUtil.errorDialog("Taki trener znajduje się już w bazie!");
            nameTextField.clear();
            surnameTextField.clear();
            clubComboBox.getSelectionModel().clearSelection();
            return;
        }
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
        if(clubComboBox2.getSelectionModel().isEmpty() || trainerComboBox.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz do usunięcia trenera z określonego klubu!");
            return;
        }
        Trainer trainer = trainerComboBox.getSelectionModel().getSelectedItem();
        HibernateUtilTrainer.removeTrainer(trainer);
        // clearing todo: this code throws an exception
        trainerComboBox.setItems(null);
        clubComboBox2.getSelectionModel().clearSelection();
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
        licenceNrColumn.setCellValueFactory(new PropertyValueFactory<>("licenceNr"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        clubColumn.setCellValueFactory(param -> new SimpleStringProperty(
                param.getValue().getClub().getName() + " " + param.getValue().getClub().getCity()));
    }
}
