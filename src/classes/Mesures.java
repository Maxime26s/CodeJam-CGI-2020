package classes;

public enum Mesures{
    LITRE("Litre"),
    GRAMME("Litre"),
    ONCE("Litre"),
    LIVRE("Litre"),
    TASSE("Litre"),
    CUILLERETHE("Litre"),
    CUILLERESOUPE("Litre");

    public final String displayName;

    private Mesures(String displayName) {
        this.displayName = displayName;
    }
}
