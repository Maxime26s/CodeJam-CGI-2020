package classes;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class Produit {
    private String nom;
    private String codeBar;
    private float prix;
    private Date dateExp;
    private int mesurePoids;
    private Mesures mesureType;

    public Produit(String nom, String codeBar, float prix, LocalDate dateExp, int mesurePoids, Mesures mesureType) {
        this.nom = nom;
        this.codeBar = codeBar;
        this.prix = prix;
        this.dateExp = java.util.Date.from(dateExp.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());;
        this.mesurePoids = mesurePoids;
        this.mesureType = mesureType;
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

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public Date getDateExp() {
        return dateExp;
    }

    public void setDateExp(Date dateExp) {
        this.dateExp = dateExp;
    }

    public int getMesurePoids() {
        return mesurePoids;
    }

    public void setMesurePoids(int mesurePoids) {
        this.mesurePoids = mesurePoids;
    }

    public Mesures getMesureType() {
        return mesureType;
    }

    public void setMesureType(Mesures mesureType) {
        this.mesureType = mesureType;
    }
}
