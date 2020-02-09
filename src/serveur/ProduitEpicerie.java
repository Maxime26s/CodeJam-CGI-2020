package serveur;

import classes.Produit;

import java.io.Serializable;

public class ProduitEpicerie implements Serializable {
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
