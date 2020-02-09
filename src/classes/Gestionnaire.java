package classes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import sample.*;
import serveur.ProduitEpicerie;

public class Gestionnaire {
    private ArrayList<ProduitInventaire> inventaire;
    private ArrayList<Recette> recettes;
    private ArrayList<Produit> produitsDisponibles;

    public Gestionnaire() {
        loadInventaire();
        loadRecettes();
        this.produitsDisponibles = new ArrayList<>();
    }

    public void checkExpiry() {
        LocalDate localDate = LocalDate.now();
        for (ProduitInventaire produit :
                this.getInventaire()) {
            if (produit.getDateExp().getYear() < localDate.getYear()) {
                produit.setExpire(true);
            } else if (produit.getDateExp().getYear() == localDate.getYear() && produit.getDateExp().getMonth() < localDate.getMonthValue()) {
                produit.setExpire(true);
            } else if (produit.getDateExp().getYear() == localDate.getYear() && produit.getDateExp().getMonth() == localDate.getMonthValue()
                    && produit.getDateExp().getDay() < localDate.getDayOfMonth()) {
                produit.setExpire(true);
            }
            int joursAvantExpiration = 0;
            joursAvantExpiration += 365 * (produit.getDateExp().getYear() - localDate.getYear());
            joursAvantExpiration += 30 * (produit.getDateExp().getMonth() - localDate.getMonthValue());
            joursAvantExpiration += (produit.getDateExp().getDay() - localDate.getDayOfMonth());
            produit.setJoursExpiration(joursAvantExpiration);
        }
    }

    public void retirerIngredients(Recette p_recette, ArrayList<ProduitInventaire> p_inventaire) {
        for (ProduitInventaire ingredientRequis :
                p_recette.getIngredientsRequis()) {
            for (ProduitInventaire ingredientInventaire :
                    p_inventaire) {
                if (ingredientRequis.getProduit().getNom().equals(ingredientInventaire.getProduit().getNom())) {
                    ingredientInventaire.setQuantite(ingredientInventaire.getQuantite() - ingredientRequis.getQuantite());
                }
            }
        }
    }

    public void saveRecettes() {
        try {
            ObjectOutputStream sortie = new ObjectOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream("recettesSaved.dat")));
            sortie.writeObject(this.recettes);
            sortie.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Fail save recettes");
        }
    }

    public void loadRecettes() {
        try {
            ObjectInputStream entree = new ObjectInputStream(
                    new BufferedInputStream(
                            new FileInputStream("recettesSaved.dat")));
            try {
                this.recettes = (ArrayList<Recette>) entree.readObject();
            } catch (ClassNotFoundException e) {

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fail load recettes");
            this.recettes = new ArrayList<>();
        }
    }

    public void saveInventaire() {
        try {
            ObjectOutputStream sortie = new ObjectOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream("gardeManger.dat")));
            sortie.writeObject(this.inventaire);
            sortie.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Fail save inventaire");
        }
    }

    public void loadInventaire() {
        try {
            ObjectInputStream entree = new ObjectInputStream(
                    new BufferedInputStream(
                            new FileInputStream("gardeManger.dat")));
            try {
                this.inventaire = (ArrayList<ProduitInventaire>) entree.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fail load inventaire");
            this.inventaire = new ArrayList<>();
        }
    }

    public void ajouterProduitInventaire(ProduitInventaire produitAAjouter){
        inventaire.add(produitAAjouter);
    }

    public void ajouterRecette(Recette recetteAAjouter) {
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

    public ArrayList<Produit> getProduitsDisponibles() {
        return produitsDisponibles;
    }

    public void setProduitsDisponibles(ArrayList<Produit> produitsDisponibles) {
        this.produitsDisponibles = produitsDisponibles;
    }
}

