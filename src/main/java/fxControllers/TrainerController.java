package fxControllers;

import accessors.converters.ClubsConverter;
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
import model.Trainer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller class for {TrainerView.fxml} file.
 * Supports operations for {@link Trainer} database table.
 * That class impements {@link Initializable} interface used with fxControllers in JavaFX.
 */
public class TrainerController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // set clubComboBox
        ObservableList<Club> clubList = FXCollections.observableArrayList();
        try {
            clubList.setAll(clubsConverter.getAll());
        } catch (IOException e) {
            e.printStackTrace();
        }
        clubComboBox.setItems(clubList);
        clubComboBox2.setItems(clubList);

        // set trainerTableView
        setPropertiesForTrainerTableView();
        ObservableList<Trainer> trainerList = FXCollections.observableArrayList();
        try {
            trainerList.setAll(trainersConverter.getAll());
        } catch (IOException e) {
            e.printStackTrace();
        }
        trainerTableView.setItems(trainerList);

        // set editable trainerTableView (for name and surname)
        trainerTableView.setEditable(true);
        // for name
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            Trainer trainer = event.getTableView().getItems().get(
                    event.getTablePosition().getRow()
            );
            if(!RegexUtil.nameRegex(event.getNewValue())) {
                DialogsUtil.errorDialog("Podano niepoprawne imię!");
            } else if(!event.getNewValue().equals("")) {
                // update trainer's name
                trainer.setName(event.getNewValue());
                try {
                    trainersConverter.update(trainer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                DialogsUtil.errorDialog("Pole nie może pozostać puste!");
            }
            event.getTableView().refresh();
        });
        // for surname
        surnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        surnameColumn.setOnEditCommit(event ->{
            Trainer trainer = event.getTableView().getItems().get(
                    event.getTablePosition().getRow()
            );
            if(!RegexUtil.surnameRegex(event.getNewValue())) {
                DialogsUtil.errorDialog("Podano niepoprawne nazwisko!");
            } else if(!event.getNewValue().equals("")) {
                // update trainer's surname
                trainer.setSurname(event.getNewValue());
                try {
                    trainersConverter.update(trainer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                DialogsUtil.errorDialog("Pole nie może pozostać puste!");
            }
            event.getTableView().refresh();
        });
    }

    /**
     * Called after pressing {@link TrainerController#addTrainerButton} and adding new {@link Trainer} to
     * the database using values selected in {@link TrainerController#nameTextField},
     * {@link TrainerController#surnameTextField} and {@link TrainerController#clubComboBox}.
     */
    @FXML public void addTrainer() throws IOException {

        // validate wrong data
        if(nameTextField.getText().isEmpty() || surnameTextField.getText().isEmpty() || clubComboBox.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wypełnij wszystkie pola formularza, aby dodać nowego trenera!");
            return;
        } else if(!RegexUtil.nameRegex(nameTextField.getText())) {
            DialogsUtil.errorDialog("Podano niepoprawne imię!");
        } else if(!RegexUtil.surnameRegex(surnameTextField.getText())) {
            DialogsUtil.errorDialog("Podano niepoprawne nazwisko!");
            surnameTextField.clear();
            return;
        }

        // add trainer
        List<Trainer> trainerBaseList = trainersConverter.getAll();
        Trainer trainer = new Trainer(
                0L,
                nameTextField.getText(),
                surnameTextField.getText(),
                clubComboBox.getSelectionModel().getSelectedItem()
        );

        // if exists
        if(trainerExists(trainerBaseList, trainer)) {
            DialogsUtil.errorDialog("Takie trener znajduje się już w bazie!");
            nameTextField.clear();
            surnameTextField.clear();
            clubComboBox.getSelectionModel().clearSelection();
            return;
        } else {
            // add
            trainersConverter.add(trainer);
        }

        // clearing
        nameTextField.clear();
        surnameTextField.clear();
        clubComboBox.getSelectionModel().clearSelection();

        // refresh view
        ObservableList<Trainer> trainerList = FXCollections.observableArrayList();
        trainerBaseList = trainersConverter.getAll();
        trainerList.setAll(trainerBaseList);
        trainerTableView.setItems(trainerList);
    }

    /**
     * Called after pressing the {@link TrainerController} and removing selected one from
     * the database using selected items in {@link TrainerController#clubComboBox2} and
     * {@link TrainerController#trainerComboBox}.
     */
    @FXML public void removeTrainer() throws IOException {

        // remove data from database
        if(clubComboBox2.getSelectionModel().isEmpty() || trainerComboBox.getSelectionModel().isEmpty()) {
            DialogsUtil.errorDialog("Wybierz do usunięcia trenera z określonego klubu!");
            return;
        }

        // remove trainer
        Trainer trainer = trainerComboBox.getSelectionModel().getSelectedItem();
        trainersConverter.remove(trainer.getLicenceNr());

        // clearing
        clubComboBox2.getSelectionModel().clearSelection();
        trainerTableView.getSelectionModel().clearSelection();
        trainerComboBox.setItems(null);

        // refresh view
        ObservableList<Trainer> trainerList = FXCollections.observableArrayList();
        List<Trainer> trainerBaseList = trainersConverter.getAll();
        trainerList.setAll(trainerBaseList);
        trainerTableView.setItems(trainerList);
    }

    /**
     * Called after select item in {@link TrainerController#clubComboBox2} and sets {@link Trainer}s
     * in {@link TrainerController#trainerComboBox}.
     */
    @FXML public void setTrainersFromSelectedClub() throws IOException {
        Club club = clubComboBox2.getSelectionModel().getSelectedItem();
        ObservableList<Trainer> trainerList = FXCollections.observableArrayList();
        List<Trainer> trainerBaseList = (club != null) ? trainersConverter.getAllByClub(club.getId()) : new ArrayList<>();
        trainerList.setAll(trainerBaseList);
        trainerComboBox.setItems(trainerList);
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

    /**
     * Checks if in the {@link List} exists {@link Trainer} with name and surname like selected one.
     * @return true if exists, false otherwise.
     */
    private boolean trainerExists(List<Trainer> trainerBaseList, Trainer trainer) {
        for(Trainer t : trainerBaseList) {
            if(t.getName().equals(trainer.getName())
                    && t.getSurname().equals(trainer.getSurname())) {
                return true;
            }
        }
        return false;
    }

    // --------------- VARIABLES --------------- //

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

    private ClubsConverter clubsConverter = new ClubsConverter();
    private TrainersConverter trainersConverter = new TrainersConverter();

}
