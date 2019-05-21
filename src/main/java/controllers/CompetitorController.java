package controllers;

import dbModels.Club;
import dbModels.Competitor;
import dbModels.Record;
import dbUtils.HibernateUtilClub;
import dbUtils.HibernateUtilCompetitor;
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

    @FXML private ComboBox<Competitor> competitorComboBox;

    @FXML private Button deleteCompetitorButton;

    @FXML private TableView<Competitor> competitorTableView;

    @FXML private TableColumn<Competitor, String> nameColumn;

    @FXML private TableColumn<Competitor, String> surnameColumn;

    @FXML private TableColumn<Competitor, String> genderColumn;

    @FXML private TableColumn<Competitor, String> clubColumn;

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

        // recordTableView
        competitionColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Record, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Record, String> param) {
                return new SimpleStringProperty(param.getValue().getCompetition().toString());
            }
        });
        timeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Record, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Record, String> param) {
                return new SimpleStringProperty(param.getValue().toString());
            }
        });
    }

    @FXML
    public void addCompetitor() {
        // add data to database
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
        Competitor competitor = competitorComboBox.getSelectionModel().getSelectedItem();
        HibernateUtilCompetitor.deleteCompetitor(competitor);
        // clearing
        competitorComboBox.getSelectionModel().clearSelection();
        // refresh view
        ObservableList<Competitor> competitorList = getCompetitors();
        competitorTableView.setItems(competitorList);
        competitorComboBox.setItems(competitorList);
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
        nameColumn.setCellValueFactory(new PropertyValueFactory<Competitor, String>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<Competitor, String>("surname"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<Competitor, String>("gender"));
        clubColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Competitor, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Competitor, String> param) {
                return new SimpleStringProperty(param.getValue().getClub().getName() + " " + param.getValue().getClub().getCity());
            }
        });
    }

    private void clearAfterAddition() {
        peselTextField.clear();
        nameTextField.clear();
        surnameTextField.clear();
        genderComboBox.getSelectionModel().clearSelection();
        clubComboBox.getSelectionModel().clearSelection();
    }
}
