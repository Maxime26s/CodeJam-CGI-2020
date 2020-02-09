package serveur;

import java.io.Serializable;

public class Revenu implements Serializable {
    private float revenu;

    public Revenu(float revenu) {
        this.revenu = revenu;
    }

    public float getRevenu() {
        return revenu;
    }

    public void setRevenu(float revenu) {
        this.revenu = revenu;
    }
}
