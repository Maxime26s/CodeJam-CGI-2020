package distributeur;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class DistributeurMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("distributeur.fxml"));
        primaryStage.setTitle("Épicerie");
        Scene mainScene = new Scene(root, 480, 400);
        mainScene.getRoot().setStyle("-fx-base:black");
        primaryStage.setScene(mainScene);
        mainScene.getStylesheets().add("modena_dark.css"); //Dark Theme: https://github.com/joffrey-bion/javafx-themes
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
