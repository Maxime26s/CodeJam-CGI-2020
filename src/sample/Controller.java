package sample;
import classes.Mesures;
import classes.Produit;
import classes.Gestionnaire;
import classes.ProduitInventaire;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class Controller {

    public static Gestionnaire gestionnaire = new Gestionnaire();

    public void addProduit(){
        LocalDate date = LocalDate.now();
        Produit produit = new Produit("nom", "1111", 10.50f, 10, Mesures.LITRE);
        gestionnaire.getInventaire().add(new ProduitInventaire(produit));
    }

    public void addInventaire(){

    }
}
