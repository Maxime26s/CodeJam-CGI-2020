package classes;

import java.time.LocalDate;
import java.util.ArrayList;
import sample.*;

public class Gestionnaire {
    private ArrayList<ProduitInventaire> inventaire;
    private ArrayList<Recette> recettes;

    public Gestionnaire() {
        this.inventaire = new ArrayList<>();
        this.recettes = new ArrayList<>();
    }

    public void checkExpiry() {
        ArrayList<ProduitInventaire> produitsExpires = new ArrayList<>();
        LocalDate localDate = LocalDate.now();
        for (ProduitInventaire produit :
                inventaire) {
            if (produit.getDateExp().getYear() <= localDate.getYear()
            || produit.getDateExp().getMonth() <= localDate.getMonthValue()
            || produit.getDateExp().getDay() <= localDate.getDayOfMonth()) {
                produit.setExpire(true);
            }
            int joursAvantExpiration = 0;
            joursAvantExpiration += 365 * (produit.getDateExp().getYear()-localDate.getYear());
            joursAvantExpiration += 30 * (produit.getDateExp().getMonth()-localDate.getMonthValue());
            joursAvantExpiration += 365 * (produit.getDateExp().getDay()-localDate.getDayOfMonth());
            produit.setJoursExpiration(joursAvantExpiration);
        }
    }


    public void retirerIngredients(Recette p_recette, ArrayList<ProduitInventaire> p_inventaire){
        for (ProduitInventaire ingredientRequis:
             p_recette.getIngredientsRequis()) {
            for (ProduitInventaire ingredientInventaire:
                 p_inventaire) {
                if (ingredientRequis.getProduit().getNom().equals(ingredientInventaire.getProduit().getNom())){
                    ingredientInventaire.setQuantite(ingredientInventaire.getQuantite()-ingredientRequis.getQuantite());
                }
            }
        }
    }

    public void ajouterProduitInventaire(ProduitInventaire produitAAjouter){
        inventaire.add(produitAAjouter);
    }

    public void ajouterRecette(Recette recetteAAjouter){
        recettes.add(recetteAAjouter);
    }

    public ArrayList<ProduitInventaire> getInventaire() {
        return inventaire;
    }

    public void setInventaire(ArrayList<ProduitInventaire> inventaire) {
        this.inventaire = inventaire;
    }

    public ArrayList<Recette> getRecettes() {
        return recettes;
    }

    public void setRecettes(ArrayList<Recette> recettes) {
        this.recettes = recettes;
    }
}
