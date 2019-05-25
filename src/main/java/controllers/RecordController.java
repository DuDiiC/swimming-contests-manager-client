package controllers;

import dbModels.Competitor;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RecordController implements Initializable {

    @FXML
    private ComboBox competitorComboBox;

    @FXML
    private ComboBox competitionComboBox;

    @FXML
    private TextField timeTextField;

    @FXML
    private Button addRecordButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

    @FXML
    public void addRecord() {

    }
}
