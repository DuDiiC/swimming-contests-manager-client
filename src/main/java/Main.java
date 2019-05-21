import dbUtils.HibernateUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        // load FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainBorderPane.fxml"));
        BorderPane borderPane = loader.load();
        Scene scene = new Scene(borderPane);

        // set database connection
        HibernateUtil.createEM();

        // set theme
        Application.setUserAgentStylesheet(STYLESHEET_CASPIAN);

        // show app
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.setTitle("Aplikacja bazodanowa");
        primaryStage.show();


    }
}
