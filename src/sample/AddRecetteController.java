package sample;

import classes.DateExpiration;
import classes.Produit;
import classes.ProduitInventaire;
import classes.Recette;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class AddRecetteController {

    @FXML
    TextField ingredientRecetteField;

    @FXML
    TextField qteField;

    @FXML
    TextArea instructionsArea;

    @FXML
    TextField nomField;

    @FXML
    CheckBox vegeBox, veganBox, halalBox;

    public ArrayList<String> ingredients = new ArrayList<>();
    public ArrayList<Float> quantites = new ArrayList<>();

    public void initialize(){
        ingredients = new ArrayList<>();
        quantites = new ArrayList<>();
    }

    public void ajouterIngredient(){
        ingredients.add(ingredientRecetteField.getText());
        ingredientRecetteField.setText("");
        quantites.add(Float.parseFloat(qteField.getText()));
        qteField.setText("");
    }

    public void ajouterRecetteAuLivre(){
        ArrayList<ProduitInventaire> produitInventaires = new ArrayList<>();
        int i = 0;
        for (String ingredient:
             ingredients) {
            for (Produit prod:
                 Main.gestionnaire.getProduitsDisponibles()) {
                if (ingredient.toUpperCase().equals(prod.getNom().toUpperCase())){
                    produitInventaires.add(new ProduitInventaire(prod, quantites.get(i), prod.getMesureType(), new DateExpiration(0,0,0)));
                }
            }
            i++;
        }
        ArrayList<String> tagos = new ArrayList<>();
        if (vegeBox.isSelected()){
            tagos.add("Vegetarien");
        }
        if (veganBox.isSelected()){
            tagos.add("Vegan");
        }
        if (halalBox.isSelected()){
            tagos.add("Halal");
        }
        Main.gestionnaire.getRecettes().add(new Recette(new ArrayList<>(produitInventaires), instructionsArea.getText(), nomField.getText(),
                new ArrayList<>(tagos)));
        Main.addRecetteStage.close();
    }
}
