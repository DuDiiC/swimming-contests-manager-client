import dbUtils.HibernateUtil;
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

        scene.getStylesheets().add(getClass().getResource("/styles/dark_mode.css").toExternalForm());

        // set database connection
        HibernateUtil.createEM();

        // set theme
        //Application.setUserAgentStylesheet(STYLESHEET_CASPIAN);

        // show app
        //primaryStage.initStyle(StageStyle.UNDECORATED);
//        primaryStage.setOpacity(0.9);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.setTitle("Swimming Contests Manager");
        primaryStage.show();
    }
}
