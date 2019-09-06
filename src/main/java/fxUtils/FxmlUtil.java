package fxUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Class for loading FXML file into the layout.
 */
public class FxmlUtil {

    /**
     * @return {@link Pane} object with new layout from file under the fxmlPath.
     */
    public static Pane fxmlLoader(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader(FxmlUtil.class.getResource(fxmlPath));
        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

