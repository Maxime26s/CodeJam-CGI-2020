package sample;

import classes.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalDate;
import java.util.ArrayList;

public class Main extends Application {
    public static Stage alimentStage = new Stage();
    public static Stage addToGardeMangerStage = new Stage();
    public static Stage addRecetteStage = new Stage();
    public static Parent alimentParent;
    public static Gestionnaire gestionnaire = new Gestionnaire();
    @Override
    public void start(Stage primaryStage) throws Exception{


        gestionnaire.getProduitsDisponibles().add(new Produit("Fat poutine", "8378932", "10", "50", "LITRES", "1"));
        gestionnaire.getProduitsDisponibles().add(new Produit("Fat chicken", "8378932", "10", "50", "GRAMMES", "1"));
        gestionnaire.getProduitsDisponibles().add(new Produit("Fat salade", "8378932", "10", "50", "CUILLIERESOUPE", "1"));
        gestionnaire.getProduitsDisponibles().add(new Produit("Fat horace", "8378932", "10", "50", "LITRES", "1"));


        /*
        public ArrayList<ProduitInventaire> recetteObjet = new ArrayList<ProduitInventaire>(gestionnaire.getProduitsDisponibles().)
        gestionnaire.getRecettes().add(new Recette())
         */


        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Cooking App");
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
