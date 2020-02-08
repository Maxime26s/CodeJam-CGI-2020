package serveur;

import classes.Mesures;
import classes.Produit;

public class ProduitEpicerie {
    Produit produit;
    int quantite;

    public ProduitEpicerie(Produit produit, int quantite) {
        this.produit = produit;
        this.quantite = quantite;
    }

    public ProduitEpicerie(Produit produit) {
        this.produit = produit;
        this.quantite = 0;
    }
}
