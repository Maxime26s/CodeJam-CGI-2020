package classes;

import java.io.Serializable;

public class Produit implements Serializable {
    private String nom;
    private String codeBar;
    private String prix;
    private String mesurePoids;
    private String mesureType;
    private String quantite;
    private String longevite;

    public Produit(String nom, String codeBar, String prix, String mesurePoids, String mesureType, String quantite, String longevite) {
        this.nom = nom;
        this.codeBar = codeBar;
        this.prix = prix;
        this.mesurePoids = mesurePoids;
        this.mesureType = mesureType;
        this.quantite = quantite;
        this.longevite = longevite;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCodeBar() {
        return codeBar;
    }

    public void setCodeBar(String codeBar) {
        this.codeBar = codeBar;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public String getMesurePoids() {
        return mesurePoids;
    }

    public void setMesurePoids(String mesurePoids) {
        this.mesurePoids = mesurePoids;
    }

    public String getMesureType() {
        return mesureType;
    }

    public void setMesureType(String mesureType) {
        this.mesureType = mesureType;
    }

    public String getQuantite() {
        return quantite;
    }

    public void setQuantite(String quantite) {
        this.quantite = quantite;
    }

    public String getLongevite() {
        return longevite;
    }

    public void setLongevite(String quantite) {
        this.longevite = longevite;
    }
}
