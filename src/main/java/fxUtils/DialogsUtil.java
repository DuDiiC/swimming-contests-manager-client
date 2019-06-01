package fxUtils;

import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

public class DialogsUtil {

    public static void errorDialog(String alertText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle("Błąd!");
        alert.setHeaderText("Błąd!");
        alert.setContentText(alertText);
        alert.showAndWait();
    }

    public static void informationDialog(String alertText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle("Informacja");
        alert.setHeaderText("Informacja");
        alert.setContentText(alertText);
        alert.showAndWait();
    }
}
