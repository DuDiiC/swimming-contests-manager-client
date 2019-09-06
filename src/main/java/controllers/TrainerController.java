package controllers;

import dbModels.Club;
import dbModels.Trainer;
import dbUtils.HibernateUtil;
import dbUtils.HibernateUtilClub;
import dbUtils.HibernateUtilTrainer;
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
 * Controller class for {TrainerView.fxml} file.
 * Supports operations for {@link Trainer} database table.
 * That class impements {@link Initializable} interface used with controllers in JavaFX.
 */
public class TrainerController implements Initializable {

    /**
     * {@link TextField} for trainer's name.
     */
    @FXML private TextField nameTextField;

    /**
     * {@link TextField} for trainer's surname.
     */
    @FXML private TextField surnameTextField;

    /**
     * {@link ComboBox} to select {@link Club} object.
     */
    @FXML private ComboBox<Club> clubComboBox;

    /**
     * {@link Button} to add a new {@link Trainer} object to the database.
     */
    @FXML private Button addTrainerButton;

    /**
     * {@link ComboBox} to select {@link Club} object.
     */
    @FXML private ComboBox<Club> clubComboBox2;

    /**
     * {@link ComboBox} to select {@link Trainer} object.
     */
    @FXML private ComboBox<Trainer> trainerComboBox;

    /**
     * {@link Button} to remove selected {@link Trainer}.
     */
    @FXML private Button removeTrainerButton;

    /**
     * {@link TableView} for {@link Trainer} class objects.
     */
    @FXML private TableView<Trainer> trainerTableView;

    /**
     * {@link TableColumn} with trainer's licence number.
     */
    @FXML private TableColumn<Trainer, Integer> licenceNrColumn;

    /**
     * {@link TableColumn} with trainer's name.
     */
    @FXML private TableColumn<Trainer, String> nameColumn;

    /**
     * {@link TableColumn} with trainer's surname.
     */
    @FXML private TableColumn<Trainer, String> surnameColumn;

    /**
     * {@link TableColumn} with trainer's club information.
     */
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
            if(!RegexUtil.nameRegex(event.getNewValue())) {
                DialogsUtil.errorDialog("Podano niepoprawne imię!");
            } else if(!event.getNewValue().equals("")) {
                HibernateUtil.getEm().getTransaction().begin();
                trainer.setName(event.getNewValue());
                HibernateUtil.getEm().getTransaction().commit();
            }
            event.getTableView().refresh();
        });
        surnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        surnameColumn.setOnEditCommit(event -> {
            Trainer trainer = event.getTableView().getItems().get(
                    event.getTablePosition().getRow()
            );
            if(!RegexUtil.surnameRegex(event.getNewValue())) {
                DialogsUtil.errorDialog("Podano niepoprawne nazwisko!");
            } else if(!event.getNewValue().equals("")) {
                HibernateUtil.getEm().getTransaction().begin();
                trainer.setSurname(event.getNewValue());
                HibernateUtil.getEm().getTransaction().commit();
            }
            event.getTableView().refresh();
        });
    }

    /**
     * Called after pressing {@link TrainerController#addTrainerButton} and adding new {@link Trainer} to
     * the database using values selected in {@link TrainerController#nameTextField},
     * {@link TrainerController#surnameTextField} and {@link TrainerController#clubComboBox}.
     */
    @FXML public void addTrainer() {
        // add data to database
        if(nameTextField.getText().isEmpty() || surnameTextField.getText().isEmpty() || clubComboBox.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wypełnij wszystkie pola formularza, aby dodać nowego trenera!");
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

    /**
     * Called after pressing the {@link TrainerController} and removing selected one from
     * the database using selected items in {@link TrainerController#clubComboBox2} and
     * {@link TrainerController#trainerComboBox}.
     */
    @FXML public void removeTrainer() {
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

    /**
     * Called after select item in {@link TrainerController#clubComboBox2} and sets {@link Trainer}s
     * in {@link TrainerController#trainerComboBox}.
     */
    @FXML public void setTrainers() {
        Club club = clubComboBox2.getSelectionModel().getSelectedItem();
        if(club != null) {
            ObservableList<Trainer> trainerList = FXCollections.observableArrayList();
            List tList = HibernateUtilClub.getAllTrainers(club);
            trainerList.setAll(tList);
            trainerComboBox.setItems(trainerList);
        }
    }

    /**
     * Retrieves information about all {@link Trainer} class objects from the database.
     * @return {@link ObservableList} with all {@link Trainer}s from the database.
     */
    private ObservableList<Trainer> getTrainer() {
        ObservableList<Trainer> trainerList = FXCollections.observableArrayList();
        trainerList.addAll(HibernateUtilTrainer.getAll());
        return trainerList;
    }

    /**
     * Sets properties for {@link TrainerController#trainerTableView}.
     */
    private void setPropertiesForTrainerTableView() {
        licenceNrColumn.setCellValueFactory(new PropertyValueFactory<>("licenceNr"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        clubColumn.setCellValueFactory(param -> new SimpleStringProperty(
                param.getValue().getClub().getName() + " " + param.getValue().getClub().getCity()));
    }
}
