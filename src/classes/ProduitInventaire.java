package classes;

import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class ProduitInventaire {
    private Produit produit;
    private float quantite;
    private Mesures typeMesure;
    private DateExpiration dateExp;
    private boolean expire;
    private int joursExpiration;

    public ProduitInventaire(Produit produit, float quantite, Mesures typeMesure, DateExpiration dateExp, boolean expire, int joursExpiration) {
        this.produit = produit;
        this.quantite = quantite;
        this.typeMesure = typeMesure;
        this.dateExp = dateExp;
        this.expire = expire;
        this.joursExpiration = joursExpiration;
    }

    public ProduitInventaire(Produit produit, float quantite, Mesures typeMesure, DateExpiration dateExp) {
        this.produit = produit;
        this.quantite = quantite;
        this.typeMesure = typeMesure;
        this.dateExp = dateExp;
    }

    public ProduitInventaire(Produit produit) {
        this.produit = produit;
        this.quantite = 0;
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

    public Mesures getTypeMesure() {
        return typeMesure;
    }

    public void setTypeMesure(Mesures typeMesure) {
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
