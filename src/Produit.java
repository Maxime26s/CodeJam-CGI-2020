import java.util.Date;

public class Produit {
    private String nom;
    private String codeBar;
    private float prix;
    private Date dateExp;
    private int mesurePoids;
    private String mesureType;

    public Produit(String nom, String codeBar, float prix, Date dateExp, int mesurePoids, String mesureType) {
        this.nom = nom;
        this.codeBar = codeBar;
        this.prix = prix;
        this.dateExp = dateExp;
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

    public String getMesureType() {
        return mesureType;
    }

    public void setMesureType(String mesureType) {
        this.mesureType = mesureType;
    }
}
