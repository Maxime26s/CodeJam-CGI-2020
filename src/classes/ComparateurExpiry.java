package classes;

import java.util.Comparator;

public class ComparateurExpiry implements Comparator<ProduitInventaire> {
    public int compare(ProduitInventaire p1, ProduitInventaire p2){
        int comparaison = p1.getJoursExpiration() - p2.getJoursExpiration();
        return comparaison;
    }
}
