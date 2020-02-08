package classes;

import java.util.ArrayList;

public class Gestionnaire {
    public ArrayList<ProduitInventaire> inventaire;
    public ArrayList<Recette> recettes;

    public Gestionnaire() {
        this.inventaire = new ArrayList<>();
        this.recettes = new ArrayList<>();
    }
}

