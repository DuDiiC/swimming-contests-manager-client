package fxUtils;

import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

import java.text.Normalizer;

/**
 * Class with {@link Alert}s for applications.
 */
public class DialogsUtil {

    /**
     * Shows error {@link Alert} on the screen.
     * @param alertText information what was wrong wiwh application.
     */
    public static void errorDialog(String alertText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle("Błąd!");
        alert.setHeaderText("Błąd!");
        alert.setContentText(alertText);
        alert.getDialogPane().getStylesheets().add(Normalizer.Form.class.getResource("/styles/mainCSS.css").toExternalForm());
        alert.showAndWait();
    }

    /**
     * Shows information {@link Alert} on the screen.
     * @param alertText text with information to display.
     */
    public static void informationDialog(String alertText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle("Informacja");
        alert.setHeaderText("Informacja");
        alert.setContentText(alertText);
        alert.getDialogPane().getStylesheets().add(Normalizer.Form.class.getResource("/styles/mainCSS.css").toExternalForm());
        alert.showAndWait();
    }
}
