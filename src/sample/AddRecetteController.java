package sample;

import classes.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class AddRecetteController {

    @FXML
    ChoiceBox ingredientRecetteField;

    @FXML
    TableView tableIngredients;

    @FXML
    TableColumn colNom;

    @FXML
    TableColumn colQuantity;

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

    public void initialize() {
        ingredients = new ArrayList<>();
        quantites = new ArrayList<>();
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        ArrayList<String> ingredientsDispo = new ArrayList<>();

        for (int i = 0; i < Main.gestionnaire.getInventaire().size(); i++)
        {
            ingredientsDispo.add(Main.gestionnaire.getInventaire().get(i).getProduit().getNom());
        }
        ingredientRecetteField.setItems(FXCollections.observableList(ingredientsDispo));
    }

    public void ajouterIngredient(){
        ingredients.add(ingredientRecetteField.getValue().toString());
        ProduitTable tempo = new ProduitTable(ingredientRecetteField.getValue().toString(),qteField.getText(),"0");
        ingredientRecetteField.setValue(null);
        quantites.add(Float.parseFloat(qteField.getText()));
        tableIngredients.getItems().add(tempo);
        qteField.setText("");
    }

    public void retireringredient()
    {
        int i = tableIngredients.getSelectionModel().getSelectedIndex();
        ingredients.remove(i);
        quantites.remove(i);
        tableIngredients.getItems().remove(i);
    }

    public void ajouterRecetteAuLivre(){
        ArrayList<ProduitInventaire> produitInventaires = new ArrayList<>();
        int i = 0;
        for (String ingredient :
                ingredients) {
            for (Produit prod :
                    Main.gestionnaire.getProduitsDisponibles()) {
                if (ingredient.toUpperCase().equals(prod.getNom().toUpperCase())) {
                    produitInventaires.add(new ProduitInventaire(prod, quantites.get(i), prod.getMesureType(), new DateExpiration(0, 0, 0)));
                }
            }
            i++;
        }
        ArrayList<String> tagos = new ArrayList<>();
        if (vegeBox.isSelected()) {
            tagos.add("Vegetarien");
        }
        if (veganBox.isSelected()) {
            tagos.add("Vegan");
        }
        if (halalBox.isSelected()) {
            tagos.add("Halal");
        }
        Main.gestionnaire.getRecettes().add(new Recette(new ArrayList<>(produitInventaires), instructionsArea.getText(), nomField.getText(),
                new ArrayList<>(tagos)));
        Main.addRecetteStage.close();
    }
}
