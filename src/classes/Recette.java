package classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Recette {
    private ArrayList<ProduitInventaire> ingredientsRequis;
    private String instructions;
    private float prix;

    public Recette(ArrayList<ProduitInventaire> ingredientsRequis, String instructions) {
        this.ingredientsRequis = ingredientsRequis;
        this.instructions = instructions;
        this.prix = calculerTotal(ingredientsRequis);
    }

    public float calculerTotal(ArrayList<ProduitInventaire> ingredients){
        int total = 0;
        for (ProduitInventaire produit:
             ingredients) {
            total += produit.getQuantite()*Integer.parseInt(produit.getProduit().getPrix());
        }
        return total;
    }

    public ArrayList<ProduitInventaire> getIngredientsRequis() {
        return ingredientsRequis;
    }

    public void setIngredientsRequis(ArrayList<ProduitInventaire> ingredientsRequis) {
        this.ingredientsRequis = ingredientsRequis;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }
}
