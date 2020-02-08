package classes;

public class ProduitInventaire {
    Produit produit;
    float quantite;
    Mesures typeMesure;

    public ProduitInventaire(Produit produit, float quantite, Mesures typeMesure) {
        this.produit = produit;
        this.quantite = quantite;
        this.typeMesure = typeMesure;
    }

    public ProduitInventaire(Produit produit) {
        this.produit = produit;
        this.quantite = 0;
    }
}
