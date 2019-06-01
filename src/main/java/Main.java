import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        // load FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainBorderPane.fxml"));
        BorderPane borderPane = loader.load();
        Scene scene = new Scene(borderPane);

//        scene.getStylesheets().add(getClass().getResource("/styles/dark_mode.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/styles/mainCSS.css").toExternalForm());
        // set theme
        //Application.setUserAgentStylesheet(STYLESHEET_CASPIAN);

        // show app
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.setTitle("Swimming Contests Manager");
        primaryStage.show();
    }
}
