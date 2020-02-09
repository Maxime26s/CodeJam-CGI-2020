package classes;

public class ProduitTable {
    String nom, quantity, prix;

    public ProduitTable(String nom, String quantity, String prix) {
        this.nom = nom;
        this.quantity = quantity;
        this.prix = prix;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }
}