package controllers;

import dbModels.Competition;
import dbModels.Contest;
import dbModels.Record;
import dbUtils.HibernateUtilCompetition;
import dbUtils.HibernateUtilContest;
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
import java.util.*;

public class CompetitionController implements Initializable {

    @FXML private ComboBox<String> styleComboBox;

    @FXML private TextField distanceTextField;

    @FXML private ChoiceBox<String> genderComboBox;

    @FXML private Button addCompetitionButton;

    @FXML private TableView<Competition> competitionTableView;

    @FXML private TableColumn<Competition, String> styleColumn;

    @FXML private TableColumn<Competition, Integer> distanceColumn;

    @FXML private TableColumn<Competition, String> genderColumn;

    @FXML private TableColumn<Competition, String> recordColumn;

    @FXML private ComboBox<Contest> contestComboBox;

    @FXML private Button addCompetitionToContestButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // styleChoiceBox
        ObservableList<String> styleList = FXCollections.observableArrayList();
        List sList = new ArrayList<String>() {
            { add("dowolny"); add("grzbietowy"); add("klasyczny"); add("motylkowy"); add("zmienny"); }
        };
        styleList.addAll(sList);
        styleComboBox.setItems(styleList);

        // genderChoiceBox
        ObservableList<String> genderList = FXCollections.observableArrayList();
        List gList = new ArrayList<String>() {
            { add("M"); add("K"); }
        };
        genderList.addAll(gList);
        genderComboBox.setItems(genderList);

        // competitionTableView
        styleColumn.setCellValueFactory(new PropertyValueFactory<Competition, String>("style"));
        distanceColumn.setCellValueFactory(new PropertyValueFactory<Competition, Integer>("distance"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<Competition, String>("gender"));
        recordColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Competition, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Competition, String> param) {
                try {
                    Record record = HibernateUtilCompetition.getBestRecord(param.getValue());
                    return new SimpleStringProperty(record.toString() + " (" + record.getCompetitor().toString()
                            + " - " + record.getCompetitor().getClub().toString() +")");
                } catch (NullPointerException e) {
                    return null;
                }
            }
        });
        competitionTableView.setItems(getCompetition());

        // contestComboBox
        ObservableList<Contest> contestList = FXCollections.observableArrayList();
        contestList.setAll(HibernateUtilContest.getAll());
        contestComboBox.setItems(contestList);
    }

    @FXML
    public void addCompetition() {
        if(Integer.valueOf(distanceTextField.getText())%25 != 0) {
            // todo: add alert
            System.out.println("Niepoprawny dystans w konkurencji - dlugosc niecki basenu to 25 m.");
            distanceTextField.clear();
        } else {
            // add data to database
            Competition competition = new Competition();
            competition.setStyle(styleComboBox.getSelectionModel().getSelectedItem());
            competition.setDistance(Integer.valueOf(distanceTextField.getText()));
            competition.setGender(genderComboBox.getSelectionModel().getSelectedItem());
            HibernateUtilCompetition.addCompetition(competition);
            // clearing
            styleComboBox.getSelectionModel().clearSelection();
            distanceTextField.clear();
            genderComboBox.getSelectionModel().clearSelection();
            // refresh view
            competitionTableView.setItems(getCompetition());
        }
    }

    @FXML
    public void removeCompetition() {
        // remove data from database
        Competition competition = HibernateUtilCompetition.getByStyleAndDistanceAndGender(
                styleComboBox.getSelectionModel().getSelectedItem(),
                Integer.valueOf(distanceTextField.getText()),
                genderComboBox.getSelectionModel().getSelectedItem()
        );
        HibernateUtilCompetition.deleteCompetition(competition);
        // clearing
        styleComboBox.getSelectionModel().clearSelection();
        distanceTextField.clear();
        genderComboBox.getSelectionModel().clearSelection();
        // refresh view
        competitionTableView.setItems(getCompetition());
    }

    @FXML
    public void addCompetitionToContest() {
        Contest contest = contestComboBox.getSelectionModel().getSelectedItem();
        Competition competition = competitionTableView.getSelectionModel().getSelectedItem();

        List<Competition> cL = contest.getCompetitions();
        cL.add(competition);
        contest.setCompetitions(cL);

        HibernateUtilContest.addOrRemoveCompetition(contest, competition);
    }

    private ObservableList<Competition> getCompetition() {
        ObservableList<Competition> competitionList = FXCollections.observableArrayList();
        competitionList.addAll(HibernateUtilCompetition.getAll());
        return competitionList;
    }
}
