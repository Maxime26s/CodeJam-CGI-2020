package classes;

import java.io.Serializable;

public enum Mesures implements Serializable {
    LITRE("Litre"),
    GRAMME("GRAMME"),
    ONCE("ONCE"),
    LIVRE("LIVRE"),
    TASSE("TASSE"),
    CUILLERETHE("CUILLERE A THE"),
    CUILLERESOUPE("CUILLERE A SOUPE");

    public final String displayName;

    private Mesures(String displayName) {
        this.displayName = displayName;
    }
}
