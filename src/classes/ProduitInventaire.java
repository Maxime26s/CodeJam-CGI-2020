package classes;


import java.io.Serializable;

public class ProduitInventaire implements Serializable {
    private Produit produit;
    private float quantite;
    private String typeMesure;
    private DateExpiration dateExp;
    private boolean expire;
    private int joursExpiration;

    public ProduitInventaire(Produit produit, float quantite, String typeMesure, DateExpiration dateExp) {
        this.produit = produit;
        this.quantite = quantite;
        this.typeMesure = typeMesure;
        this.dateExp = dateExp;
        this.expire = false;
        this.joursExpiration = 0;
    }


    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public float getQuantite() {
        return quantite;
    }

    public void setQuantite(float quantite) {
        this.quantite = quantite;
    }

    public String getTypeMesure() {
        return typeMesure;
    }

    public void setTypeMesure(String typeMesure) {
        this.typeMesure = typeMesure;
    }

    public DateExpiration getDateExp() {
        return dateExp;
    }

    public void setDateExp(DateExpiration dateExp) {
        this.dateExp = dateExp;
    }

    public boolean isExpire() {
        return expire;
    }

    public void setExpire(boolean expire) {
        this.expire = expire;
    }

    public int getJoursExpiration() {
        return joursExpiration;
    }

    public void setJoursExpiration(int joursExpiration) {
        this.joursExpiration = joursExpiration;
    }
}
